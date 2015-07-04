import os
import json
import requests
import urllib2

# print "Hello!!!"
dd = {}
# final = []
print "["
with open('qr_accessibility.json') as data_file:
    data = json.load(data_file)
    for d in data:
        # print d
        dd = d
        # try:
        # print dd
        top_level_url = "https://opia.api.translink.com.au/v2/location/rest/suggest?input="+d['Station']+"+Station&filter=1&maxResults=1&api_key=special-key"
        response = requests.get(top_level_url, auth=('christie.ethan', '/6Y)=anqE2_x'), headers={'content-type': 'application/json', 'accept': 'application/json'})
        data = response.json()
        # print data
        dd['stationid'] = data["Suggestions"][0]['Id']

        top_level_url = "https://opia.api.translink.com.au/v2/location/rest/stops-at-landmark/"+d['stationid']+"?api_key=special-key"
        response = requests.get(top_level_url, auth=('christie.ethan', '/6Y)=anqE2_x'), headers={'content-type': 'application/json', 'accept': 'application/json'})
        data = response.json()
        dd['stopIds'] = data['StopIds']

        top_level_url = "https://opia.api.translink.com.au/v2/location/rest/stops?ids="+d['stopIds'][0]+"&api_key=special-key"
        response = requests.get(top_level_url, auth=('christie.ethan', '/6Y)=anqE2_x'), headers={'content-type': 'application/json', 'accept': 'application/json'})
        data = response.json()
        dd['position'] = data['Stops'][0]['Position']

            # dd = d
            
            # final.append(dd)
        # except:
            # continue
       
        print json.dumps(dd)
        print ","
print "]"