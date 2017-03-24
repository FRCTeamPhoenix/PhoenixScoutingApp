import argparse
import json
import csv
from os.path import isfile as fileExists

parser = argparse.ArgumentParser()
parser.add_argument("srcFiles", nargs='*', help="The JSON files to add to the spreadsheet")

args = parser.parse_args()
sourceData = args.srcFiles

allData = []

for j in sourceData:
    if fileExists(j):
        data = json.load(open(j, 'r'))
        allData += data
    else:
        print("\"{}\" is not a valid file".format(j))

freshFile = not fileExists("output.csv")

with open("output.csv", 'w', newline='') as f:
    #While it is possible to grab the fields from the dictionary's keys
    #that would not guarantee the order will be consistent and does not
    #account for missing fields
    fields = [
        "teamNumber",
        "teamRating",
        "canClimb",
        "gearLoaded",
        "gearFloor",
        "shootsLow",
        "shootsHigh",
        "autoStation1",
        "autoStation2",
        "autoStation3",
        "autoDepositGear",
        "shootsAuto",
        "comments"
    ]
    writer = csv.DictWriter(f, fields)

    writer.writeheader()
    writer.writerows(allData)
