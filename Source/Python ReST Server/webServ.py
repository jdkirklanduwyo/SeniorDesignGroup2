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
    #This is standard request format: /current
    '/current', 'cur_plant',
    #This is standard request format: /add/plant/ptype light water humid temp health
    #                            E.G: host:8080/add/plant/1 4 3 45 78 100
    '/add/plant/(.*)', 'add_plant',
    #This is standard request format (No Spaces in categories): /add/type/name light water humid temp family size foliage foliagecolor heatzone lightrange soilrange waterrange fertilizer
    #                            E.G: host:8080/add/type/BeanSprout 4 3 45 78 bean 8-12in leafy 3 2 1 2-4 4-6 6
    '/add/type/(.*)', 'add_type',
    #This is standard request format: /update/id ptype light water humid temp health
    #                            E.G: host:8080/update/plant/7 1 4 3 45 78 100
    '/update/plant/(.*)', 'update_plant',
    #This is standard request format: /update/id name light water humid temp family size foliage foliagecolor heatzone lightrange soilrange waterrange fertilizer
    #                            E.G: host:8080/update/type/1 BeanSprout 4 3 45 78 bean 8-12in leafy 3 2 1 2-4 4-6 6
    '/update/type/(.*)', 'update_type',
    #This is standard request format: /update/current/id ptype light water humid temp health
    #                            E.G: host:8080/update/current/7 1 4 3 45 78 100
    #Only update current AFTER plants have been updated to prevent differing data
    '/update/current/(.*)', 'update_current'
)

app = web.application(urls, globals())

class plant_types:        
    def GET(self):
        print 'Plant types'
        output = 'settings:[';
        for child in root.findall('planttype'):
            for childp in child:
                print 'childp', childp.tag, childp.attrib
                output += str(childp.attrib) + ','
        output += ']';
        return output
    
class get_type:        
    def GET(self, type):
        print 'Get type'
        output = 'settings:[';
        for child in root.findall('planttype'):
            for childp in child:
                if childp.attrib['id'] == type:
                    return str(childp.attrib)

class get_plant:
    def GET(self, plant):
        print 'Get plant'
        for child in root.findall('activeplants'):
            for childp in child:
                if childp.attrib['id'] == plant:
                    return str(childp.attrib)
                        
class cur_plant:
    def GET(self):
        print 'Current plant'
        for child in root.findall('curplant'):
            return str(child.attrib)

class add_plant:
    def GET(self, data):
        print 'Add plant'
        for child in root.findall('activeplants'):
            npID = int(child.attrib['id'])
            data = map(int, data.split())
            nRoot = ET.SubElement(child, 'plant', attrib={'id':str(npID), 'ptype':str(data[0]), 'light':str(data[1]), 'water':str(data[2]), 'humid':str(data[3]), 'temp':str(data[4]), 'health':str(data[5])})
            child.set('id', str(npID+1))
            ET.dump(root)
            tree.write('plant_data.xml')
            return 'Success'
        return 'Failure'

class add_type:
    def GET(self, data):
        print 'Add Type'
        for child in root.findall('planttype'):
            ntID = int(child.attrib['id'])
            data = map(str, data.split())
            print data
            nRoot = ET.SubElement(child, 'ptype', attrib={'id':str(ntID), 'name':data[0], 'light':data[1], 'water':data[2], 'humid':data[3], 'temp':data[4], 'family':data[5],
                'size':data[6], 'foliage':data[7], 'foliagecolor':data[8], 'heatzone':data[9], 'lightrange':data[10], 'soilr':data[11], 'waterr':data[12], 'fertilizer':data[13]})
            child.set('id', str(ntID+1))
            ET.dump(root)
            tree.write('plant_data.xml')
            return 'Success'
        return 'Failure'

class update_plant:
    def GET(self, data):
        print 'Update plant'
        plant = map(int, data.split())
        for child in root.findall('activeplants'):
            for childp in child:
                if childp.attrib['id'] == str(plant[0]):
                    childp.set('ptype', str(plant[1]))
                    childp.set('light', str(plant[2]))
                    childp.set('water', str(plant[3]))
                    childp.set('humid', str(plant[4]))
                    childp.set('temp', str(plant[5]))
                    childp.set('health', str(plant[6]))
                    tree.write('plant_data.xml')
                    return 'Success'
        return 'Failure'
        
class update_type:
    def GET(self, data):
        print 'Update type'
        type = data.split()
        for child in root.findall('planttype'):
            for childp in child:        
                if childp.attrib['id'] == type[0]:
                    childp.set('name', type[1])
                    childp.set('light', type[2])
                    childp.set('water', type[3])
                    childp.set('humid', type[4])
                    childp.set('temp', type[5])
                    childp.set('family', type[6])
                    childp.set('size', type[7])
                    childp.set('foliage', type[8])
                    childp.set('foliagecolor', type[9])
                    childp.set('heatzone', type[10])
                    childp.set('lightrange', type[11])
                    childp.set('soilr', type[12])
                    childp.set('waterr', type[13])
                    childp.set('fertilizer', type[14])
                    tree.write('plant_data.xml')
                    return 'Success'
        return 'Failure'
        
class update_current:
    def GET(self, data):
        print 'Update cur plant'
        plant = data.split()
        for child in root.findall('curplant'):
            child.set('id', plant[0])
            child.set('ptype', plant[1])
            child.set('light', plant[2])
            child.set('water', plant[3])
            child.set('humid', plant[4])
            child.set('temp', plant[5])
            child.set('health', plant[6])
            tree.write('plant_data.xml')
            return 'Success'
        return 'Failure'
                
if __name__ == "__main__":
    app.run()