#!/bin/bash

# usage info
memory_free=$(vmstat --unit M | tail -1 | awk -v col="4" '{print $col}')
cpu_idle=$(vmstat | tail -1 | awk '{print $14}')
cpu_kernel=$(vmstat | tail -1 | awk '{print $13}')
disk_io=$(vmstat --unit M -d | tail -1 | awk -v col="10" '{print $col}')
disk_available=$(df -h / | awk '{print $4}')

echo "cpu_idle: $disk_available"