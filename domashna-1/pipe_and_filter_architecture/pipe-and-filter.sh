#!/bin/bash
./database.sh $1 $2 $3 $4 && osmfilter $4 --keep="amenity=parking and name= " | osmconvert - --all-to-nodes --csv="@id @lat @lon name capacity access fee operator website supervised parking" --csv-separator=","  | uniq > uniq_parkings.csv && psql -p $2 -h $1 -U $3 -d parking_spots -c "\copy parking from 'uniq_parkings.csv' delimiter ',' csv"
