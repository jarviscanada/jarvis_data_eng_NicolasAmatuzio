#!/bin/bash

# Setup and validate arguments (again, don't copy comments)
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# Check # of args
if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

# Save machine statistics in MB and current machine hostname to variables
lscpu_out=$(lscpu)
hostname=$(hostname -f)

# Retrieve hardware specification variables
# xargs is a trick to trim leading and trailing white spaces
cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | egrep "^Architecture:(\s)+x86_64" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | grep "^Model name:" | awk '{print $3, $4, $5, $6, $7}' | xargs)
cpu_mhz=$(echo "$lscpu_out" | grep "^Model name:" | awk '{print $7 * 1000}' | tr -d 'A-Za-z' | xargs)
l2_cache=$(echo "$lscpu_out" | grep "^L2" | awk '{print $3}' | xargs)
total_mem=$(vmstat --unit M | tail -1 | awk '{print $4}')

# Current time in `2019-11-26 14:40:19` UTC format
timestamp=$(vmstat -t | tail -1 | awk '{print $18, $19}')

# PSQL command: Inserts server usage data into host_usage table
# Note: be careful with double and single quotes
insert_stmt="INSERT INTO host_info(
               hostname, cpu_number, cpu_architecture,
               cpu_model, cpu_mhz, l2_cache, timestamp,
               total_mem
             )
             VALUES
               (
                 '$hostname', '$cpu_number', '$cpu_architecture',
                 '$cpu_model', '$cpu_mhz', '$l2_cache',
                 '$timestamp', '$total_mem'
               )"

#set up env var for pql cmd
export PGPASSWORD=$psql_password
#Insert date into a database
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?