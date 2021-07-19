-- # user data
create table if not exists users
(
    username      varchar(30)  not null,
    password      varchar(128) not null,
    password_salt varchar(11) not null,
    first_name    varchar(30)  not null,
    last_name     varchar(30)  not null,
    primary key (username)
);
# meeting data
create table if not exists meeting
(
    id_meeting int AUTO_INCREMENT,
    name       varchar(30) not null,
    date       datetime    not null,
    duration   float       not null,
    admin      varchar(30) not null,
    foreign key (admin) references users (username),
    primary key (id_meeting)
);
# invitation status for the participants
create table meeting_participants
(
    id_meeting          int not null,
    username            varchar(30) not null,
    invitation_status   varchar(10) check (invitation_status in ('open','approved','declined')),
    date                datetime,
    primary key (id_meeting, username)
);
# meeting chat
create table meeting_comments
(
    id_meeting int not null,
    id_user    int not null,
    date       datetime,
    msg        text,
    primary key (id_meeting,id_user, date)
);
# notification when admin changes meeting info
create table user_notification
(
    id_notification int not null AUTO_INCREMENT,
    username        varchar(30) not null,
    msg             text,
    viewed          boolean,
    primary key (id_notification)
);
DROP TRIGGER IF EXISTS meeting_changed_trigger;
# This trigger is handle the notifications to the participants when the admin change a meeting
CREATE TRIGGER meeting_changed_trigger
    AFTER UPDATE on meeting for each row
    BEGIN
    DECLARE msg VARCHAR(100);
    DECLARE count_notifications boolean;
#     check if the meeting has changed more than one time on the last 1 hour
    set count_notifications =
        (SELECT DISTINCT(count(id_notification)>0) as count_notifications
        FROM user_notification
        WHERE user_notification.id_meeting = OLD.id_meeting AND date > NOW() - INTERVAL 1 HOUR
        group by username);
    if count_notifications = 1 THEN
        set msg = ' changed the meeting more than one time in the last hour, check it out please';
        DELETE FROM user_notification
        WHERE user_notification.id_meeting=OLD.id_meeting AND
                date > NOW() - INTERVAL 1 HOUR;
        INSERT INTO user_notification(id_meeting, username, msg, date,viewed)
            (SELECT OLD.id_meeting, meeting_participants.username , concat(OLD.admin, msg), now() , false
             FROM (meeting_participants)
             WHERE (OLD.id_meeting = meeting_participants.id_meeting) AND invitation_status = 'approved');
    ELSE
#       Detect the edited field
        IF (OLD.name <> NEW.name) THEN
            SET msg = ' changed the meeting name ';
        ELSEIF (OLD.description <> NEW.description) THEN
            set msg = ' changed the meeting description ';
        ELSEIF (OLD.date <> NEW.date) THEN
            set msg = ' changed the meeting date';
        ELSEIF (OLD.duration <> NEW.duration) THEN
            set msg = ' changed the meeting duration';
        ELSE
            set msg = ' changed the meeting';
        end if;
#         send a notification to all participants
        INSERT INTO user_notification(id_meeting, username, msg, date,viewed)
            (SELECT OLD.id_meeting ,meeting_participants.username , concat(OLD.admin ,msg), now() , false
             FROM (meeting_participants)
             WHERE (OLD.id_meeting = meeting_participants.id_meeting) AND invitation_status = 'approved');

    end if;
    END;
# This trigger inform the participants for their invitations
CREATE TRIGGER meeting_invitations_trigger
    AFTER INSERT on meeting_participants for each row
    INSERT INTO user_notification(username, msg, date,viewed)
        (SELECT meeting_participants.username,
                concat(meeting.admin,' sends you a invitation for the meeting: ',meeting.name),NOW(),false
            FROM meeting natural join meeting_participants
            WHERE id_meeting = NEW.id_meeting);
