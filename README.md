(Criação do banco de dados, utilizando o Firebird + FlameRobin)

CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(200),
    email VARCHAR(200) UNIQUE,
    senha VARCHAR(100)
);

CREATE TABLE miniatura (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idUser INT,
    lugar VARCHAR(200),
    estado VARCHAR(200),
    pais VARCHAR(100),
    data DATE,
    status VARCHAR(100),
    foto VARCHAR(200),
    FOREIGN KEY (idUser) REFERENCES usuario(id) ON DELETE CASCADE
);
