import web
import xml.etree.ElementTree as ET

tree = ET.parse('plant_data.xml')
root = tree.getroot()

urls = (
    '/settings', 'current_settings',
    '/plant/(.*)', 'get_plant'
)

app = web.application(urls, globals())

class current_settings:        
    def GET(self):
	output = 'settings:[';
	for child in root:
                print 'child', child.tag, child.attrib
                output += str(child.attrib) + ','
	output += ']';
        return output

class get_plant:
    def GET(self, plant):
	for child in root:
		if child.attrib['id'] == plant:
		    return str(child.attrib)

if __name__ == "__main__":
    app.run()