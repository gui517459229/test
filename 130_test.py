import urllib2
import sys
url = 'http://www.qcloud.com'
try:
    req = urllib2.Request(url)
    rep = urllib2.urlopen(req)
    the_page = rep.read()
except urllib2.URLError,e:
    print "cuowu:",e
    sys.exit()
print the_page

