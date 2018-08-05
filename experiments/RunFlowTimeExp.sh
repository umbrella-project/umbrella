#!/bin/bash

controller="onos"

min=45
max=45
step=5
runs=2

outputFile="FlowTimeExpOutput-${controller}.csv"

rm -f $outputFile

for n in `seq $min $step $max`
do
	echo "Running experiment with linear topology of size $n"
	echo -e -n "\tRuns ($runs): "
	echo -n "$n" >> $outputFile

	for r in `seq 1 $runs`
	do
		echo -n -e " ..$r"

		rm -f ./capture-output

		sudo python FlowTimeExp.py $controller $n > /dev/null 2>&1

		rcvd=`cat ./capture-output`

		echo -n ",$rcvd" >> $outputFile
	done

	echo ""

	echo "" >> $outputFile
done
