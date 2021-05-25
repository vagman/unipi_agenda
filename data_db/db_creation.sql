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
    id_meeting        int not null,
    username          varchar(30) not null,
    invitation_status varchar(10) check (invitation_status in ('open','approved','declined')),
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
