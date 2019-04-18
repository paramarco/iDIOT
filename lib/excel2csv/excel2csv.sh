
LIST="AREA_CONTOURS_GUIDE_DATA_TABLE.xls INTEREST_VOLUMES_DATA_TABLE.xls AREA_CONTOUR_POINTS_DATA_TABLE.xls RESPONSIBILITY_VOLUMES_DATA_TABLE.xls BASIC_SECTORS_DATA_TABLE.xls SECTOR_VOLUMES_DATA_TABLE.xls HOLDING_AIRSPACE_VOLUMES_DATA_TABLE.xls
AIRSPACE_VOLUMES_GUIDE_DATA_TABLE.xls HOLDING_VOLUMES_GUIDE_DATA_TABLE.xls ROUTE_CONDITIONS_GUIDE_DATA_TABLE.xls SITUATION_LINE_CONDITIONS_DATA_TABLE.xls SITUATION_LINES_GUIDE_DATA_TABLE.xls SITUATION_LINE_POINTS_DATA_TABLE.xls"

mkdir excel2csv
mkdir excel2csv/tmp

for filename in $LIST; do
	echo "my filename: ${filename}" ;
    java -jar excel2csv-1.0.2.jar -f 1 $filename ./excel2csv/tmp/ ;
    sleep 2s;
    cp ./excel2csv/tmp/1-* ./excel2csv/"${filename%%.*}".csv ;
    rm -rf ./excel2csv/tmp/* ;
done

rm -rf ./excel2csv/tmp

