CREATE TABLE transactions (
    id VARCHAR(255) PRIMARY KEY,
    amount DECIMAL(10, 2) NOT NULL,
    payer_id VARCHAR(255) NOT NULL,
    payee_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (payer_id) REFERENCES accounts (id),
    FOREIGN KEY (payee_id) REFERENCES accounts (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
