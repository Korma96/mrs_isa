insert into user values('AD', 101, 'ia0', 0, 1, 'ia0', 'markoradovic996@gmail.com', 'ia', 'ia', 'rrr', 123);
insert into user values('AD', 102, 'ia1', 0, 1, 'ia1', 'markoradovic996@gmail.com', 'ia', 'ia', 'rrr', 123);
insert into user values('AD', 103, 'ia2', 0, 1, 'ia2', 'markoradovic996@gmail.com', 'ia', 'ia', 'rrr', 123);
insert into user values('AD', 104, 'fa', 0, 1, 'fa', 'markoradovic996@gmail.com', 'fa', 'fa', 'rrr', 123);
insert into user values('AD', 105, 'sa', 0, 1, 'sa', 'markoradovic996@gmail.com', 'sa', 'sa', 'rrr', 123);
insert into user values('AD', 201, 'ia20', 0, 2, 'ia20', 'markoradovic996@gmail.com', 'ia', 'ia', 'rrr', 123);
insert into user values('AD', 202, 'ia21', 0, 2, 'ia21', 'markoradovic996@gmail.com', 'ia', 'ia', 'rrr', 123);
insert into user values('AD', 203, 'ia22', 0, 2, 'ia22', 'markoradovic996@gmail.com', 'ia', 'ia', 'rrr', 123);
insert into user values('AD', 204, 'fa2', 0, 2, 'fa2', 'markoradovic996@gmail.com', 'fa', 'fa', 'rrr', 123);
insert into user values('AD', 205, 'sa2', 0, 2, 'sa2', 'markoradovic996@gmail.com', 'sa', 'sa', 'rrr', 123);
insert into user values('RU', 301, 'ia3', 0, 0, 'ia3', 'markoradovic996@gmail.com', 'ia', 'ia', 'rrr', 123);
insert into user values('RU', 302, 'ia4', 0, 0, 'ia4', 'markoradovic996@gmail.com', 'ia', 'ia', 'rrr', 123);
insert into user values('RU', 303, 'ia5', 0, 0, 'ia5', 'markoradovic996@gmail.com', 'ia', 'ia', 'rrr', 123);
insert into user values('RU', 304, 'fa3', 0, 0, 'fa3', 'markoradovic996@gmail.com', 'fa', 'fa', 'rrr', 123);
insert into user values('RU', 305, 'sa3', 0, 0, 'sa3', 'markoradovic996@gmail.com', 'sa', 'sa', 'rrr', 123);
insert into user values('SA', 405, 'sa4', 0, 3, 'sa4', '', '', '', '', '');
insert into user values('SA', 406, 'sa5', 0, 3, 'sa5', '', '', '', '', '');
insert into user values('SA', 407, 'sa6', 0, 3, 'sa6', '', '', '', '', '');

insert into cultural_institution values(1,'Pozorisni trg 1, Novi Sad','Promotivni opis 1','Srpsko narodno pozoriste', 1);
insert into cultural_institution values(2,'Bulevar Mihajla Pupina 3, Novi Sad','Promotivni opis 2','Arena Cineplex', 0);
insert into cultural_institution values(3,'Sentandrejski put 11, Novi Sad','Promotivni opis 3','CineStar', 0);
insert into cultural_institution values(4,'Ignjata Pavlasa 4, Novi Sad','Promotivni opis 4','Pozoriste mladih', 1); 

insert into auditorium values(1,'Sala 1',7,8);
insert into auditorium values(2,'Sala 2',7,8);
insert into auditorium values(3,'Sala 3',7,8);
insert into auditorium values(4,'Sala 4',7,8);
insert into auditorium values(5,'Sala 5',10,10);
insert into auditorium values(6,'Sala 6',10,10);
insert into auditorium values(7,'Sala 7',10,10);
insert into auditorium values(8,'Sala 8',10,10);
insert into auditorium values(9,'Sala 9',8,9);
insert into auditorium values(10,'Sala 10',8,9);

insert into cultural_institution_auditoriums values(1,1);
insert into cultural_institution_auditoriums
values(2,2);
insert into cultural_institution_auditoriums values(3,3);
insert into cultural_institution_auditoriums values(4,4);
insert into cultural_institution_auditoriums values(1,5);
insert into cultural_institution_auditoriums values(2,6);
insert into cultural_institution_auditoriums values(3,7);
insert into cultural_institution_auditoriums values(4,8);
insert into cultural_institution_auditoriums values(1,9);
insert into cultural_institution_auditoriums values(2,10);

insert into showing values(1,8.0,120,'Action','Aridatha Wyatt, Marna House, Kaylee Marler or martin, Willetta Freeman, Olwen Kerslake, Conny Blee, Shela Hewitt','Military Fiction','Shir Goodsell-Johnson','','Description1');
insert into showing values(2,8.0,150,'Drama','Lisabeth Havelock, Carley Parker, Malvina Savills','Black Swan','Joyce Glendinin','','Description2');
insert into showing values(3,7.5,163,'Horror','Malva De la puelba, Dell Wadham, Laverne Sowermire, Ludovika Hewitt','Ghostland','Abbe Wyatt','','Description3');
insert into showing values(4,6.8,185,'Comedy','Gwenette Fielder, Darsie Faithful, Florrie Freeman','Repo Man','Jorrie De Aldburgh','','Description4');
insert into showing values(5,8.1,124,'Adventure','Rianon Deering, Etta Milbanke, Kylynn Pettigrew, Albina Mcphail, Briana Gow','Bruc','Cosetta Orton','','Description5');
insert into showing values(6,5.7,150,'Comedy','Francisca Roethke, Shell Philips, Gilberte Neal, Gillian Troup','Paul','Con Carna','','Description6');
insert into showing values(7,8.3,136,'Action','Shelly Deering, Nannie Jeffrey, Millie Oversby obersby, Lucie Smithies, Germaine Jeffeaux','Assassins','Juliane Farrelly','','Description7');
insert into showing values(8,7.5,140,'Comedy','Reina Elkin, Gelya Hutchins, Stephine Blee','Space Jam','Hildegarde Oversby Obersby','','Description8');
insert into showing values(9,8.1,166,'Action','Donia Atkinson, Moina Mcallister, Florencia Lockett','Blades','Gill De cabaret','','Description9');
insert into showing values(10,7.9,174,'Adventure','Susanna Heslop, Claudia Butler, Minta Benham, Elianore Fielder','Centurion','Nadiya Ashley','','Description10');
insert into showing values(11,5.7,152,'Adventure','Doe De carteret, Camella De la puelba, Pepi Chamley','Robin Hood','Darb Atkinson','','Description11');
insert into showing values(12,6.9,132,'History','Berri Wight, Nelia Wyatt, Clarice Mcgehee, Dorolisa Glendinin','Spotlight','Dolli Benham','','Description12');

insert into cultural_institution_showings values(3,1);
insert into cultural_institution_showings values(4,4);
insert into cultural_institution_showings values(1,1);
insert into cultural_institution_showings values(2,6);
insert into cultural_institution_showings values(3,4);
insert into cultural_institution_showings values(4,10);
insert into cultural_institution_showings values(1,9);
insert into cultural_institution_showings values(2,10);