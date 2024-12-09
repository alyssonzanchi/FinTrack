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
    image VARCHAR(100) DEFAULT '/images/profile_images/user.png',
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
    closing INT NOT NULL CHECK (closing BETWEEN 1 AND 31),
    payment INT NOT NULL CHECK (payment BETWEEN 1 AND 31),
    user_id INT NOT NULL,
    account_id INT NOT NULL,
    CONSTRAINT "creditcard_pk" PRIMARY KEY (id),
    CONSTRAINT "fk_creditcard_user_id_users" FOREIGN KEY (user_id) REFERENCES "Users"(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT "fk_creditcard_account_id_accounts" FOREIGN KEY (account_id) REFERENCES "Accounts"(id) ON DELETE NO ACTION ON UPDATE CASCADE
);

CREATE TABLE "Invoices" (
    id SERIAL,
    creditcard_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    due_date DATE NOT NULL,
    total DECIMAL(10, 2) DEFAULT 0.00,
    paid BOOLEAN DEFAULT FALSE,
    CONSTRAINT "invoices_pk" PRIMARY KEY (id),
    CONSTRAINT "fk_invoices_creditcard_id" FOREIGN KEY (creditcard_id) REFERENCES "CreditCard"(id) ON DELETE CASCADE ON UPDATE CASCADE
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
    name VARCHAR(100) NOT NULL,
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
    credit_card_id INT,
    invoice_id INT,
    user_id INT NOT NULL,
    CONSTRAINT "transactions_pk" PRIMARY KEY (id),
    CONSTRAINT "fk_transactions_user_id_users" FOREIGN KEY (user_id) REFERENCES "Users"(id) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT "fk_transactions_account_id_accounts" FOREIGN KEY (account_id) REFERENCES "Accounts"(id) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT "fk_transactions_category_id_categories" FOREIGN KEY (category_id) REFERENCES "Categories"(id) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT "fk_transactions_credit_card_id" FOREIGN KEY (credit_card_id) REFERENCES "CreditCards"(id) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT "fk_transactions_invoice_id" FOREIGN KEY (invoice_id) REFERENCES "Invoices"(id) ON DELETE SET NULL ON UPDATE CASCADE
);

INSERT INTO "Categories" (type, name, icon) VALUES
('RECEITA', 'Pagamentos', '/images/categories/dolar.png'),
('RECEITA', 'Benefícios', '/images/categories/food.png'),
('RECEITA', 'Fixa Mensal', '/images/categories/note.png'),
('RECEITA', 'Outros', '/images/categories/other.png'),
('RECEITA', 'Comissão', '/images/categories/percent.png'),
('RECEITA', 'Rendimentos', '/images/categories/bank.png'),
('RECEITA', 'Serviços', '/images/categories/tool.png'),
('RECEITA', 'Vendas', '/images/categories/store.png'),
('DESPESA', 'Carro', '/images/categories/car.png'),
('DESPESA', 'Alimentação', '/images/categories/food.png'),
('DESPESA', 'Educação', '/images/categories/education.png'),
('DESPESA', 'Família', '/images/categories/family.png'),
('DESPESA', 'Lazer', '/images/categories/leisure.png'),
('DESPESA', 'Moradia', '/images/categories/house.png'),
('DESPESA', 'Outros', '/images/categories/other.png'),
('DESPESA', 'Pagamentos', '/images/categories/dolar.png'),
('DESPESA', 'Saúde', '/images/categories/health.png'),
('DESPESA', 'Serviços', '/images/categories/note.png'),
('DESPESA', 'Transporte', '/images/categories/bus.png'),
('DESPESA', 'Vestuário', '/images/categories/hanger.png');
