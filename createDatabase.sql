create database book_library;
use book_library;

create table book
(
    isbn int not null,
    cover varchar(255) null,
    title varchar(50) not null,
    publisher varchar(50) not null,
    publishDate date not null,
    pageCount int not null,
    description varchar(150) not null,
    totalAmount int not null,
    id int auto_increment,
    constraint book_pk
        primary key (id)
);

create unique index book_isbn_uindex
    on book (isbn);

create unique index book_title_uindex
    on book (title);

create table borrow_record
(
    bookId int not null,
    readerId int not null,
    borrowDate date not null,
    timePeriodId int not null,
    returnDate date null,
    comment varchar(150) null,
    statusId int null,
    id int auto_increment,
    constraint borrow_record_pk
        primary key (id)
);

create table borrow_status
(
    id int auto_increment,
    status varchar(25) not null,
    constraint book_status_pk
        primary key (id)
);

create unique index borrow_status_status_uindex
    on borrow_status (status);

insert into borrow_status(status) values ('returned'), ('returned and damaged'), ('lost'), ('not returned');

create table borrow_period
(
    id int auto_increment,
    months int not null,
    constraint book_status_pk
        primary key (id)
);

create unique index borrow_period_months_uindex
    on borrow_period (months);

insert into borrow_period(months) values (1),(2),(3),(6),(12);

alter table borrow_record
    add constraint borrow_record_borrow_status_id_fk
        foreign key (statusId) references borrow_status (id);

alter table borrow_record
    add constraint borrow_record_time_period_id_fk
        foreign key (timePeriodId) references borrow_period (id);

alter table borrow_record
    add constraint borrow_record_book_id_fk
        foreign key (bookId) references book (id);

create table reader
(
    firstName varchar(15) not null,
    lastName varchar(20) not null,
    email varchar(50) not null,
    gender varchar(15) not null,
    phoneNumber int null,
    registrationDate date not null,
    id int auto_increment,
    constraint reader_pk
        primary key (id)
);

alter table borrow_record
    add constraint borrow_record_reader_email_fk
        foreign key (readerId) references reader (id);

create table book_author
(
    id int auto_increment,
    book_id int not null,
    author varchar(50) not null,
    constraint book_authors_pk
        primary key (id),
    constraint book_author_book_id_fk
        foreign key (book_id) references book (id)
);

create table book_genre
(
    id int auto_increment,
    book_id int not null,
    genre varchar(30) not null,
    constraint book_genres_pk
        primary key (id),
    constraint book_genre_book_id_fk
        foreign key (book_id) references book (id)
);