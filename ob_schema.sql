drop database if exists ob;
create database ob;
use ob;

-- table branches structure --
create table branches(
    branch_id int not null auto_increment,
    branch_name varchar(50),
    branch_address varchar(255),
    status int default 0,
    created_at datetime default now(),
    updated_at datetime default now(),

    primary key(branch_id),
    constraint c_branch_name unique(branch_name)
);

-- table customers structure --
create table customers(
    customer_id int not null auto_increment,
    branch_id int not null,
    first_name varchar(50),
    last_name varchar(50),
    gender varchar(50),
    identity_number varchar(255),
    identity_type varchar(50),
    identity_issue varchar(50),
    identity_expiration date,
    birth_date date,
    birth_place varchar(50),
    nationality varchar(50),
    residence varchar(50),
    address varchar(255),
    post_code varchar(50),
    phone varchar(50),
    email varchar(50),
    profession varchar(255),
    profile_picture varchar(255),
    signature_picture varchar(255),
    identity_picture varchar(255),
    status int default 0,
    created_at datetime default now(),
    updated_at datetime default now(),

    primary key(customer_id),
    foreign key(branch_id) references branches(branch_id),
    constraint c_identity_number unique(identity_number)
);

-- table accounts structure --
create table accounts(
    account_id int not null auto_increment,
    account_name varchar(255),
    account_number varchar(255),
    account_type varchar(50), -- saving/business
    account_profile varchar(50), -- personal/joint/associative/staff/system
    balance decimal(10,2) default 0.0,
    currency varchar(50),
    status int default 0,
    created_at datetime default now(),
    updated_at datetime default now(),

    primary key(account_id),
    constraint c_account_number unique(account_number)
);

-- table vaults structure --
create table vaults(
    vault_id int not null auto_increment,
    account_id int not null,
    branch_id int not null,

    primary key(vault_id),
    foreign key(account_id) references accounts(account_id),
    foreign key(branch_id) references branches(branch_id)
);

-- table trunks structure --
create table trunks(
    trunk_id int not null auto_increment,
    account_id int not null,
    customer_id int not null,
    membership varchar(50), -- owner/jointed/associated

    primary key(trunk_id),
    foreign key(account_id) references accounts(account_id),
    foreign key(customer_id) references customers(customer_id),
    constraint c_trunk unique(account_id, customer_id)
);

-- table transactions structure --
create table transactions(
    transaction_id int not null auto_increment,
    transaction_type varchar(50), -- deposit/withdraw/transfer
    account_id int not null,
    amount decimal(10,2),
    currency varchar(50),
    source_type varchar(50), -- Online/ATM(Visa,Mastercard)/Cheque
    source_value varchar(255), -- Branch 1/***2485 number of card/cheque
    reason varchar(255),
    failure_reason varchar(255),
    status int default 0,
    initiated_by varchar(50),
    updated_by varchar(50),
    created_at datetime default now(),
    updated_at datetime default now(),

    primary key(transaction_id),
    foreign key(account_id) references accounts(account_id)
);

-- table payments structure --
create table payments(
    payment_id int not null auto_increment,
    debit_account_id int not null,
    credit_account_id int not null,
    amount decimal(10,2),
    currency varchar(50),
    reason varchar(255),
    failure_reason varchar(255),
    status int default 0,
    initiated_by varchar(50),
    updated_by varchar(50),
    created_at datetime default now(),
    updated_at datetime default now(),

    primary key(payment_id),
    foreign key(debit_account_id) references accounts(account_id),
    foreign key(credit_account_id) references accounts(account_id)
);

-- table exchanges structure --
create table exchanges(
    exchange_id int not null auto_increment,
    customer_name varchar(255),
    customer_identity varchar(255),
    sale_currency varchar(50),
    buy_currency varchar(50),
    amount decimal(10,2),
    reason varchar(255),
    failure_reason varchar(255),
    status int default 0,
    initiated_by varchar(50),
    updated_by varchar(50),
    created_at datetime default now(),
    updated_at datetime default now(),

    primary key(exchange_id)
);

-- table loans structure --
create table loans(
    loan_id int not null auto_increment,
    loan_type varchar(50), -- gold/salary/other
    account_id int not null,
    currency varchar(50),
    issued_amount decimal(10,2),
    interest_rate decimal(10,2),
    duration int,
    total_amount decimal(10,2),
    per_month_amount decimal(10,2),
    reason varchar(255),
    failure_reason varchar(255),
    status int default 0,
    initiated_by varchar(50),
    updated_by varchar(50),
    created_at datetime default now(),
    updated_at datetime default now(),

    primary key(loan_id),
    foreign key(account_id) references accounts(account_id),
    constraint c_loan_account unique(account_id)
);

-- table borrowers structure --
create table borrowers(
    borrower_id int not null auto_increment,
    loan_id int not null,
    customer_id int not null,

    primary key(borrower_id),
    foreign key(loan_id) references loans(loan_id),
    foreign key(customer_id) references customers(customer_id),
    constraint c_borrowing unique(loan_id, customer_id)
);

-- table cards structure --
create table cards(
    card_id int not null auto_increment,
    account_id int not null,
    card_type varchar(50), -- visa/mastercard/american express
    card_number varchar(255),
    card_name varchar(255),
    expiration_date date,
    cvc varchar(4),
    cvc_checked int default 0,
    status int default 0,
    initiated_by varchar(50),
    updated_by varchar(50),
    failure_reason varchar(255),
    created_at datetime default now(),
    updated_at datetime default now(),

    primary key(card_id),
    foreign key(account_id) references accounts(account_id),
    constraint c_card_number unique(card_number)
);

-- table card application requests structure --
create table card_applications(
    request_id int not null auto_increment,
    account_id int not null,
    card_type varchar(50),
    status int default 0,
    failure_reason varchar(255),
    initiated_by varchar(50),
    updated_by varchar(50),
    created_at datetime default now(),
    updated_at datetime default now(),

    primary key(request_id),
    foreign key(account_id) references accounts(account_id)
);

-- table cheques structure --
create table cheques(
    cheque_id int not null auto_increment,
    account_id int not null,
    cheque_name varchar(255),
    cheque_number varchar(255),
    status int default 0,
    initiated_by varchar(50),
    updated_by varchar(50),
    created_at datetime default now(),
    updated_at datetime default now(),

    primary key(cheque_id),
    foreign key(account_id) references accounts(account_id),
    constraint c_cheque_number unique(cheque_number)
);

-- view vaults structure --
create view v_vaults
as
select
a.account_id,
a.account_name,
a.account_number,
a.account_type,
a.account_profile,
a.balance,
a.currency,
a.status,
a.created_at,
a.updated_at,
v.vault_id,
v.branch_id
from 
    accounts as a
inner join vaults as v
    on v.account_id = a.account_id
inner join branches as b
    on b.branch_id = v.branch_id;

-- view trunks structure --
create view v_trunks
as
select
a.account_id,
a.account_name,
a.account_number,
a.account_type,
a.account_profile,
a.balance,
a.currency,
a.status,
a.created_at,
a.updated_at,
t.trunk_id,
t.customer_id,
t.membership
from 
    accounts as a
inner join trunks as t
    on t.account_id = a.account_id
inner join customers as c
    on c.customer_id = t.customer_id;

-- view transactions structure --
create view v_transactions
as
select
t.transaction_id,
t.transaction_type,
t.account_id,
t.amount,
t.currency,
t.source_type,
t.source_value,
t.reason,
t.failure_reason,
t.status,
t.initiated_by,
t.updated_by,
t.created_at,
t.updated_at,
a.account_name,
a.account_number
from
    transactions as t
inner join accounts as a
    on a.account_id = t.account_id
order by t.created_at desc;

-- view payments structure --
create view v_payments
as
select
p.payment_id,
p.debit_account_id,
p.credit_account_id,
p.amount,
p.currency,
p.reason,
p.failure_reason,
p.status,
p.initiated_by,
p.updated_by,
p.created_at,
p.updated_at,
a1.account_name as debit_account_name,
a1.account_number as debit_account_number,
a2.account_name as credit_account_name,
a2.account_number as credit_account_number
from
    payments as p
inner join accounts as a1
    on a1.account_id = p.debit_account_id
inner join accounts a2
    on a2.account_id = p.credit_account_id
order by p.created_at desc;

-- view loans structure --
create view v_loans
as
select
l.loan_id,
l.loan_type,
l.account_id,
l.currency,
l.issued_amount,
l.interest_rate,
l.duration,
l.total_amount,
l.per_month_amount,
l.reason,
l.failure_reason,
l.status,
l.initiated_by,
l.updated_by,
l.created_at,
l.updated_at,
b.borrower_id,
b.customer_id,
c.first_name,
c.last_name,
c.identity_number,
a.account_name,
a.account_number,
a.balance as remaining_amount
from
    loans as l
inner join borrowers as b
    on b.loan_id = l.loan_id
inner join customers as c
    on c.customer_id = b.customer_id
inner join accounts as a
    on a.account_id = l.account_id
order by l.created_at desc;

-- view cards structure --
create view v_cards
as
select
c.card_id,
c.account_id,
c.card_type,
c.card_number,
c.card_name,
c.expiration_date,
c.cvc,
c.cvc_checked,
c.status,
c.initiated_by,
c.updated_by,
c.failure_reason,
c.created_at,
c.updated_at,
a.account_name,
a.account_number
from
    cards as c
inner join accounts as a
    on a.account_id = c.account_id
order by c.created_at desc;

-- view cheques structure --
create view v_cheques
as
select
c.cheque_id,
c.account_id,
c.cheque_name,
c.cheque_number,
c.status,
c.initiated_by,
c.updated_by,
c.created_at,
c.updated_at,
a.account_name,
a.account_number
from
    cheques as c
inner join accounts as a
    on a.account_id = c.account_id
order by created_at desc;

-- view card_applications structure --
create view v_card_applications
as
select
c.request_id,
c.account_id,
c.card_type,
c.status,
c.failure_reason,
c.initiated_by,
c.updated_by,
c.created_at,
c.updated_at,
a.account_name,
a.account_number
from
    card_applications as c
inner join accounts as a
    on a.account_id = c.account_id
order by created_at desc;