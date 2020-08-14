create table post (id serial primary key, name character (200) not null, text text, link text UNIQUE, created timestamp); 
