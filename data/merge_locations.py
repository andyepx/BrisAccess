import os
import json

import requests

final = []
dd = {}

'''
with open('locations_accessibility.json') as data_file:
    data = json.load(data_file)
    with open('data.addresses.json') as data_file2:    
        data2 = json.load(data_file2)
        for d in data:
            dd = d
            for d2 in data2:
                #print d['location'] + ' == ' + d2['title']
                if d['location'] == d2['title']:
 		    dd['address'] = d2['address']
            final.append(dd)


'''

#https://opia.api.translink.com.au/v2/location/rest/resolve?input=114%20Grey%20St%20South%20Brisbane%20QLD%204101,%20South%20Brisbane%22&filter=0&maxResults=1&api_key=special-key

import urllib2

with open('final_locations_data.json') as data_file:
    data = json.load(data_file)
    for d in data:
        try:
            top_level_url = "https://opia.api.translink.com.au/v2/location/rest/resolve?input="+d['address']+"&filter=0&maxResults=1"
            response = requests.get(top_level_url, auth=('christie.ethan', '/6Y)=anqE2_x'), headers={'content-type': 'application/json', 'accept': 'application/json'})
            data = response.json()
            dd = d
            d['position'] = data['Locations'][0]['Position']
            final.append(dd)
        except:
            continue
       
        print json.dumps(dd)
