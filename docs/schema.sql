DROP TABLE IF EXISTS "Transactions";
DROP TABLE IF EXISTS "Categories";
DROP TABLE IF EXISTS "CreditCard";
DROP TABLE IF EXISTS "Accounts";
DROP TABLE IF EXISTS "Users";

CREATE TABLE "Users" (
    id SERIAL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(60) NOT NULL,
    name VARCHAR(50) NOT NULL,
    date_of_birth DATE,
    image VARCHAR(100),
    CONSTRAINT "user_pk" PRIMARY KEY (id),
    CONSTRAINT "unique_email" UNIQUE (email)
);

CREATE TABLE "Accounts" (
    id SERIAL,
    name VARCHAR(50) NOT NULL,
    balance DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    icon VARCHAR(50),
    user_id INT NOT NULL,
    CONSTRAINT "account_pk" PRIMARY KEY (id),
    CONSTRAINT "fk_accounts_user_id_users" FOREIGN KEY (user_id) REFERENCES "Users"(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE "CreditCard" (
    id SERIAL,
    name VARCHAR(50) NOT NULL,
    icon VARCHAR(50),
    "limit" DECIMAL(10, 2) NOT NULL,
    closing INT NOT NULL,
    payment INT NOT NULL,
    user_id INT NOT NULL,
    account_id INT NOT NULL,
    CONSTRAINT "creditcard_pk" PRIMARY KEY (id),
    CONSTRAINT "fk_creditcard_user_id_users" FOREIGN KEY (user_id) REFERENCES "Users"(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT "fk_creditcard_account_id_accounts" FOREIGN KEY (account_id) REFERENCES "Accounts"(id) ON DELETE NO ACTION ON UPDATE CASCADE
);

CREATE TABLE "Categories" (
    id SERIAL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('RECEITA', 'DESPESA')),
    name VARCHAR(100) NOT NULL,
    icon VARCHAR(50),
    CONSTRAINT "categories_pk" PRIMARY KEY (id)
);

CREATE TABLE "Transactions" (
    id SERIAL,
    type VARCHAR(15) NOT NULL CHECK (type IN ('RECEITA', 'DESPESA')),
    amount DECIMAL(10, 2) NOT NULL,
    "date" DATE NOT NULL,
    description TEXT,
    recurring VARCHAR(15) CHECK (recurring IN ('FIXA', 'PARCELADA')),
    fixed_frequency VARCHAR(20),
    installment_frequency VARCHAR(20),
    installment_count INT,
    category_id INT NOT NULL,
    account_id INT NOT NULL,
    CONSTRAINT "transactions_pk" PRIMARY KEY (id),
    CONSTRAINT "fk_transactions_account_id_accounts" FOREIGN KEY (account_id) REFERENCES "Accounts"(id) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT "fk_transactions_category_id_categories" FOREIGN KEY (category_id) REFERENCES "Categories"(id) ON DELETE SET NULL ON UPDATE CASCADE
);
