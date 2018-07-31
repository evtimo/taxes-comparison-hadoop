DROP TABLE seller;
DROP TABLE customer;


CREATE TABLE IF NOT EXISTS seller ( seller_inn varchar(10), seller_kpp varchar(9), customer_inn varchar(10),customer_kpp varchar(9), total_with_tax double, total_without_tax double) COMMENT
'Sellers transaction info' ROW FORMAT DELIMITED FIELDS TERMINATED BY '\\t' LINES TERMINATED BY  '\n' STORED AS TEXTFILE;


CREATE TABLE IF NOT EXISTS customer (  customer_inn varchar(10), customer_kpp varchar(9), seller_inn varchar(10),seller_kpp varchar(9),  total_with_tax double, total_without_tax double) COMMENT
 'Sellers transaction info' ROW FORMAT DELIMITED FIELDS TERMINATED BY '\\t' LINES TERMINATED BY  '\n' STORED AS TEXTFILE;
