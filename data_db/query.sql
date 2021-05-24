--Όνομα και επίθετο χρήστη
Select first_name, last_name
from users
where username=' ';

--Participants of meeting (with name)
Select username
from meeting_participant inner join meetings
where name=' ';

--Τα meeting του χρηστη τα οποια εχει αποδεχτει 
Select name
From meeting_participants inner join meeting
Where username=' ' AND inivitation_status='approved';

--Τα meeting τα οποία ειναι ανοιχτα
Select name
From meeting_participants inner join meeting
Where username=' ' AND inivitation_status='open';

--Τα σχόλια ενός meeting
Select msg
From meeting_comments inner join meeting 
Where name=' ';

--Τα σχολια ενός χρήστη σε ένα συγκεκριμένο meeting
Select msg
From meeting_comments inner join meeting
Where name=' ' AND id_user=' ';


