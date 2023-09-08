CREATE TABLE accounts (
    id BINARY(16) PRIMARY KEY,
    full_name VARCHAR(120) NOT NULL,
    document VARCHAR(14) UNIQUE NOT NULL,
    email VARCHAR(60) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    balance DECIMAL(10, 2) DEFAULT 20,
    account_type VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
