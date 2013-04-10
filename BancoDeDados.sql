DROP TABLE IF EXISTS Usuario CASCADE;
DROP TABLE IF EXISTS Livro CASCADE;
DROP TABLE IF EXISTS Emprestimo CASCADE;
DROP TABLE IF EXISTS Mensagem CASCADE;

CREATE TABLE Usuario(
nome VARCHAR (50) NOT NULL,
email VARCHAR (50) NOT NULL,
matricula INT NOT NULL,
senha VARCHAR (50) NOT NULL,

PRIMARY KEY (matricula)

);

CREATE TABLE Livro(
codLivro INT NOT NULL,
dono INT NOT NULL,
titulo VARCHAR (50),
autor VARCHAR (50),
genero VARCHAR (50),
editora VARCHAR (50),
ano INT,

PRIMARY KEY (codLivro),
FOREIGN KEY (dono) REFERENCES Usuario (matricula)

);

CREATE TABLE Emprestimo(
id INT NOT NULL,
livroEmp INT NOT NULL,
usuEmp INT NOT NULL,
estado VARCHAR (15),
status VARCHAR (20),
dataEmprestimo DATE NOT NULL,
dataDevolucao DATE NOT NULL,
dataVencimento DATE NOT NULL,

PRIMARY KEY (id),
FOREIGN KEY (livroEmp) REFERENCES Livro (codLivro),
FOREIGN KEY (usuEmp) REFERENCES Usuario (matricula)

);

CREATE TABLE Mensagem(
id INT NOT NULL,
remetente INT NOT NULL,
destinatario INT NOT NULL,
dataEnvio DATE,
recado TEXT NOT NULL,

PRIMARY KEY (id),
FOREIGN KEY (remetente) REFERENCES Usuario (matricula), 
FOREIGN KEY (destinatario) REFERENCES Usuario (matricula)

);
