# SCRIPTS SQL AZURE TABLES

DROP TABLE USUARIO;

CREATE TABLE USUARIO (
    CODIGO VARCHAR(20) NOT NULL,
    USERNAME VARCHAR(20)  NOT NULL , 
    PASS VARCHAR(20)  NOT NULL ,
    TIPO VARCHAR(10) NOT NULL,
    ACTIVADO VARCHAR(5) NOT NULL,
    PRIMARY KEY (CODIGO)
)

DROP TABLE FINCA;

CREATE TABLE FINCA (
    CODIGO VARCHAR(20) NOT NULL,
    NOMBRE VARCHAR(20)  NOT NULL , 
    COLOR VARCHAR(20) NOT NULL,
    USUARIO VARCHAR(20) NOT NULL,
    PRIMARY KEY (CODIGO),
    FOREIGN KEY (USUARIO) REFERENCES USUARIO(CODIGO)
);

DROP TABLE VARIEDAD;

CREATE TABLE VARIEDAD (
    CODIGO VARCHAR(20) NOT NULL,
    NOMBRE VARCHAR(20)  NOT NULL , 
    PRIMARY KEY (CODIGO),
);

DROP TABLE ARTICULO;

CREATE TABLE ARTICULO (
    CODIGO VARCHAR(20) NOT NULL,
    NOMBRE VARCHAR(20)  NOT NULL ,
    PRIMARY KEY (CODIGO),
);


DROP TABLE CONJUNTO_ARTICULOS;

CREATE TABLE CONJUNTO_ARTICULOS (
    CODIGO INT IDENTITY(1,1),
    NOMBRE VARCHAR(20) NOT NULL ,
    ARTICULO VARCHAR(20) NOT NULL,
    PRIMARY KEY (CODIGO),
    FOREIGN KEY (ARTICULO) REFERENCES ARTICULO(CODIGO)
);

DROP TABLE TIPO_OPERACION;

CREATE TABLE TIPO_OPERACION (
    CODIGO VARCHAR(20) NOT NULL,
    NOMBRE VARCHAR(20)  NOT NULL , 
    TIENE_ARTICULOS VARCHAR(20) NOT NULL,
    COLOR VARCHAR(20) NOT NULL,
    PRIMARY KEY (CODIGO),
);

DROP TABLE PARCELA;

CREATE TABLE PARCELA (
    CODIGO VARCHAR(20) NOT NULL,
    NOMBRE VARCHAR(20) NOT NULL , 
    CONJUNTA VARCHAR(20) NOT NULL ,
    VARIEDAD VARCHAR(20) NOT NULL,
    FINCA VARCHAR(20) NOT NULL,
    PRIMARY KEY (CODIGO),
    FOREIGN KEY (VARIEDAD) REFERENCES VARIEDAD(CODIGO),
    FOREIGN KEY (FINCA) REFERENCES FINCA(CODIGO)
);

DROP TABLE COORDENADAS_PARCELA;

CREATE TABLE COORDENADAS_PARCELA (
    CODIGO INT IDENTITY(1,1),
    LAT FLOAT NOT NULL,
    LONG FLOAT NOT NULL ,
    PARCELA VARCHAR(20) NOT NULL,
    PRIMARY KEY (CODIGO),
    FOREIGN KEY (PARCELA) REFERENCES PARCELA(CODIGO)
);

CREATE TABLE CONJUNTO_PARCELAS (
    CODIGO INT IDENTITY(1,1),
    NOMBRE VARCHAR(20) NOT NULL ,
    PARCELA VARCHAR(20) NOT NULL,
    PRIMARY KEY (CODIGO),
    FOREIGN KEY (PARCELA) REFERENCES PARCELA(CODIGO)
);


DROP TABLE OPERACION;

CREATE TABLE OPERACION (
    CODIGO VARCHAR(20) NOT NULL,
    NOMBRE VARCHAR(20) NOT NULL , 
    COLOR VARCHAR(20) NOT NULL,
    TIPO VARCHAR(20) NOT NULL,
    FECHA VARCHAR(20) NOT NULL,
    ARTICULOS VARCHAR(20) NOT NULL,
    OBSERVACIONES VARCHAR(200) NOT NULL,
    TROZOS INT NOT NULL,
    PARCELA VARCHAR(20) NOT NULL,
    FINCA VARCHAR(20) NOT NULL,
    USUARIO VARCHAR(20) NOT NULL,
    PRIMARY KEY (CODIGO),
    FOREIGN KEY (TIPO) REFERENCES TIPO_OPERACION(CODIGO),
    FOREIGN KEY (USUARIO) REFERENCES USUARIO(CODIGO)
);


DROP TABLE COORDENADAS_OPERACION;

CREATE TABLE COORDENADAS_OPERACION(
    CODIGO INT IDENTITY(1,1),
    LAT FLOAT NOT NULL,
    LONG FLOAT NOT NULL ,
    TROZO INT NOT NULL,
    OPERACION VARCHAR(20) NOT NULL,
    PRIMARY KEY (CODIGO),
    FOREIGN KEY (OPERACION) REFERENCES OPERACION(CODIGO)
);

CREATE TABLE CONJUNTO_OPERACIONES (
    CODIGO INT IDENTITY(1,1),
    NOMBRE VARCHAR(20) NOT NULL ,
    OPERACION VARCHAR(20) NOT NULL,
    PRIMARY KEY (CODIGO),
    FOREIGN KEY (OPERACION) REFERENCES OPERACION(CODIGO)
);


