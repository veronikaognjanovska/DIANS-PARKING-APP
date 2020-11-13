#!/bin/bash

if [ $# -ne 4 ]
then
	echo "USAGE '$0' <database_host_ip> <database_port_number> <database_user> <data_source_path>"
	exit -1
fi;

host=$1
port=$2
user=$3
data_source=$4

#installation of command line tools osmctools for csv converter
sudo apt install osmctools

#connect to postgresql database
database_exists=`psql -p $port -h $host -U $user -c 'SELECT datname FROM pg_database WHERE datistemplate = false;' | grep 'parking_spots' | wc -l`
if [ $database_exists -ne 1 ]
then
	psql -p $port -h $host -U $user -c 'CREATE DATABASE parking_spots;'
fi

#create table if it doesnt exist
table_exists=`psql -p $port -h $host -U $user -d parking_spots -c 'SELECT table_schema,table_name FROM information_schema.tables ORDER BY table_schema,table_name;' | grep 'parking' | wc -l`
if [ $table_exists -lt 1 ]
then 
	psql -p $port -h $host -U $user -d parking_spots -c 'CREATE TABLE parking(id varchar(255) primary key, lat double precision not null, lng double precision not null, name varchar(255), capacity integer, access varchar(10), fee varchar(10), operator varchar(255), website varchar(1024), supervised varchar(25), parking_type varchar(50));'
fi

echo "Database creation finished..."
exit