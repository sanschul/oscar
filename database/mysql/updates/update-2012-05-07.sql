create table tickler_text_suggest (id int(10) not null AUTO_INCREMENT, creator varchar(6) not null, suggested_text varchar(255) not null, create_date timestamp not null, active tinyint(1) not null, PRIMARY KEY (id));

insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Advised Test Results", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Advised RTC see INFO", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Advised RTC see MD", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Advised RTC for Rx", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Advised RTC for Lab Work", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Advised RTC for immunization", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Declined treatment", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Don't call", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Letter sent", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Msg on ans. mach. to call clinic", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Msg with roomate to call clinic", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Phone - No Answer", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Notified", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Notified. Patient is asymptomatic.", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Prescription given", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Prescription phoned in to:", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Referral Booked", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Re-Booked for followup", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Returned for Lab Work", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Telephone Busy", now(), "1");