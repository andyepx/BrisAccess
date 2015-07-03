import os
import urllib2

html = ""
for x in range(1,47):
    response = urllib2.urlopen('http://www.brisbane-stories.webcentral.com.au/access/08_locations/index.asp?page='+str(x))
    html += response.read() 

print html
