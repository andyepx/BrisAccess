import os
import json
import requests
import urllib2


dd = {}
print "["
with open('brispost.json') as data_file:
    data = json.load(data_file)
with open('suburbs_accessibility.json') as data_file2:
    data2 = json.load(data_file2)
    
    for d2 in data2:
        dd = d2
        for d in data:
            if dd['Suburb_Name'].lower() == d['SUBURB'].lower():
                dd['position_lat'] = d['LAT']
                dd['position_lng'] = d['LNG']
        print json.dumps(dd)
        print ","
print "]"