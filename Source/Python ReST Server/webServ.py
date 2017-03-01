import web
import xml.etree.ElementTree as ET

tree = ET.parse('plant_data.xml')
root = tree.getroot()

urls = (
    #All Plant Types
    '/data/types', 'plant_types',
    #This is standard request format: /data/id#
    '/data/(.*)', 'get_type',
    #This is standard request format: /plant/id#
    '/plant/(.*)', 'get_plant',
    #This is standard request format: /add/plant/ptype light water humid temp health
    #                            E.G: host:8080/add/plant/1 4 3 45 78 100
    '/add/plant/(.*)', 'add_plant',
    #This is standard request format (No Spaces in categories): /add/type/name light water humid temp family size foliage foliagecolor heatzone lightrange soilrange waterrange fertilizer
    #                            E.G: host:8080/add/type/BeanSprout 4 3 45 78 bean 8-12in leafy 3 2 1 2-4 4-6 6
    '/add/type/(.*)', 'add_type',
    #This is standard request format: /update/id ptype light water humid temp health
    #                            E.G: host:8080/update/7 1 4 3 45 78 100
    '/update/(.*)', 'update_plant'
)

app = web.application(urls, globals())

class plant_types:        
    def GET(self):
        print 'Plant types'
        output = 'settings:[';
        for child in root:
            if child.tag=='planttype':
                for childp in child:
                    print 'childp', childp.tag, childp.attrib
                    output += str(childp.attrib) + ','
        output += ']';
        return output
    
class get_type:        
    def GET(self, type):
        print 'Get type'
        output = 'settings:[';
        for child in root:
            if child.tag=='planttype':
                for childp in child:
                    if childp.attrib['id'] == type:
                        return str(childp.attrib)

class get_plant:
    def GET(self, plant):
        print 'Get plant'
        for child in root:
            if child.tag=='activeplants':
                for childp in child:
                    if childp.attrib['id'] == plant:
                        return str(childp.attrib)

class add_plant:
    def GET(self, data):
        print 'Add plant'
        for child in root:
            if child.tag=='activeplants':
                npID = int(child.attrib['id'])
                data = map(int, data.split())
                nRoot = ET.SubElement(child, 'plant', attrib={'id':str(npID), 'ptype':str(data[0]), 'light':str(data[1]), 'water':str(data[2]), 'humid':str(data[3]), 'temp':str(data[4]), 'health':str(data[5])})
                child.set('id', str(npID+1))
                ET.dump(root)
                tree.write('plant_data.xml')

class add_type:
    #Untested
    def GET(self, data):
        print 'Add Type'
        for child in root:
            if child.tag=='planttype':
                ntID = int(child.attrib['id'])
                data = map(int, data.split())
                nRoot = ET.SubElement(child, 'ptype', attrib={'id':str(ntID), 'name':str(data[0]), 'light':str(data[1]), 'water':str(data[2]), 'humid':str(data[3]), 'temp':str(data[4]), 'family':str(data[5]),
                    'size':str(data[6]), 'foliage':str(data[7]), 'foliagecolor':str(data[8]), 'heatzone':str(data[9]), 'lightrange':str(data[10]), 'soilr':str(data[11]), 'waterr':str(data[12]), 'fertilizer':str(data[13])})
                child.set('id', str(ntID+1))
                ET.dump(root)
                tree.write('plant_data.xml')

class update_plant:
    def GET(self, data):
        print 'Update'
        plant = map(int, data.split())
        print plant
        for child in root:
            if child.tag=='activeplants':
                for childp in child:
                    if childp.attrib['id'] == str(plant[0]):
                        childp.set('ptype', str(plant[1]))
                        childp.set('light', str(plant[2]))
                        childp.set('water', str(plant[3]))
                        childp.set('humid', str(plant[4]))
                        childp.set('temp', str(plant[5]))
                        childp.set('health', str(plant[6]))
                        ET.dump(child)
                        tree.write('plant_data.xml')

if __name__ == "__main__":
    app.run()
