﻿create table post (id serial primary key, name text not null, text text, link text UNIQUE, created timestamp);
