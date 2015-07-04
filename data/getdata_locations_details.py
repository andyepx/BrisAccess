import os
import urllib2
from lxml import html
from lxml.cssselect import CSSSelector
import json

outdata = []

for x in range(1,10000):
    response = urllib2.urlopen('http://www.brisbane-stories.webcentral.com.au/access/08_locations/location.asp?ID='+str(x))
    tree = html.fromstring(response.read())

    sel = CSSSelector('span.heading')
    try:
        if sel(tree)[0].text != 'None':
            title = sel(tree)[0].text
    except:
        title = ''

    #sel = tree.xpath('/table/tbody/tr/td/text()')
    sel = CSSSelector('table table table table td:first-of-type')
    address = ''
    #print sel(tree)[0].text
    try:
        if sel(tree)[57].text != 'None':
            address = sel(tree)[57].text
    except:
        address = ''


    #i = 0
    #for c in sel(tree):
    #	print str(i) + '  ' + str(c.text)
    #    i+=1
 
    d = {}
    d['address'] = address
    d['title'] = title
    print json.dumps(d)

#print json.dumps(outdata)
