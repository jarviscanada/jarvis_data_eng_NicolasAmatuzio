#!/bin/bash
# save hostname as a variable
lscpu_out=`lscpu`
# hardware info
hostname=$(hostname -f)
cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | egrep "^Architecture:(\s)+x86_64" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | grep "^Model name:" | awk '{print $3, $4, $5}' | xargs)
cpu_mhz=$(echo "$lscpu_out" | grep "^Model name:" | awk '{print $7}' | xargs)
l2_cache=$(echo "$lscpu_out" | grep "^L2" | awk -F: '{print $2}' | xargs)
total_mem= $(vmstat --unit M | tail -1 | awk '{print $4}')
timestamp= $(date "+%Y-%m-%d %H:%M:%S")# current timestamp in `2019-11-26 14:40:19` format; use `date` cmd

echo "$cpu_number" | echo "$cpu_architecture"
echo "Cpu Model: $cpu_model"
echo "mzh: $cpu_mhz"
echo "L2: $l2_cache"
echo "total mem: $total_mem"