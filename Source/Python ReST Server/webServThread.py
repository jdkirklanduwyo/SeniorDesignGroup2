import web
import threading
import time
import xml.etree.cElementTree as ET

tree = ET.parse('plant_data.xml')
root = tree.getroot()

urls = (
    #All Plant Types
    '/data/types', 'plant_types',
    #All Plants
    '/plants', 'all_plants',
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
    '/update/current/(.*)', 'update_current',
    #TODO: Remove Plant and Type
    #This is standard request format: /remove/plant/id#
    '/remove/plant/(.*)', 'remove_plant',
    #This is standard request format: /remove/type/id#
    '/remove/type/(.*)', 'remove_type'
    
)

app = web.application(urls, globals())

class plant_types:        
    def GET(self):
        print 'Plant types'
        output = 'settings:[';
        for child in root.findall('planttype'):
            for childp in child:
                #print 'childp', childp.tag, childp.attrib
                output += str(childp.attrib) + ','
        output += ']';
        return output

class all_plants:        
    def GET(self):
        print 'All Plants'
        output = '[';
        for child in root.findall('activeplants'):
            for childp in child:
                #print 'childp', childp.tag, childp.attrib
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
            #print data
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
                    tree = ET.ElementTree(root)
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
                    tree = ET.ElementTree(root)
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
            tree = ET.ElementTree(root)
            tree.write('plant_data.xml')
            return 'Success'
        return 'Failure'
        
class remove_plant:        
    def GET(self, plant):
        print 'remove_plant'
        print plant
        output = 'settings:[';
        for child in root.findall('activeplants'):
            for childp in child:
                if childp.attrib['id'] == plant:
                    child.remove(childp)
                    tree = ET.ElementTree(root)
                    tree.write('plant_data.xml')
                    return "Success"
            return "Failure"

class remove_type:
    def GET(self, type):
        print 'remove_type'
        for child in root.findall('planttype'):
            for childp in child:
                if childp.attrib['id'] == type:
                    child.remove(childp)
                    tree = ET.ElementTree(root)
                    tree.write('plant_data.xml')
                    return "Success"
            return "Failure"

###############################
#   This comes from the implementation of
#   http://machinelearningmastery.com/implement-decision-tree-algorithm-scratch-python/
#   with modifications by us       
#


# Split a dataset based on an attribute and an attribute value
def test_split(index, value, dataset):
	left, right = list(), list()
	for row in dataset:
		if row[index] < value:
			left.append(row)
		else:
			right.append(row)
	return left, right

# Calculate the Gini index for a split dataset
def gini_index(groups, class_values):
	gini = 0.0
	for class_value in class_values:
		for group in groups:
			size = len(group)
			if size == 0:
				continue
			proportion = [row[-1] for row in group].count(class_value) / float(size)
			gini += (proportion * (1.0 - proportion))
	return gini

# Select the best split point for a dataset
def get_split(dataset):
	class_values = list(set(row[-1] for row in dataset))
	b_index, b_value, b_score, b_groups = 999, 999, 999, None
	for index in range(len(dataset[0])-1):
		for row in dataset:
			groups = test_split(index, row[index], dataset)
			gini = gini_index(groups, class_values)
			if gini < b_score:
				b_index, b_value, b_score, b_groups = index, row[index], gini, groups
	return {'index':b_index, 'value':b_value, 'groups':b_groups}

# Create a terminal node value
def to_terminal(group):
	outcomes = [row[-1] for row in group]
	return max(set(outcomes), key=outcomes.count)

# Create child splits for a node or make terminal
def split(node, max_depth, min_size, depth):
	left, right = node['groups']
	del(node['groups'])
	# check for a no split
	if not left or not right:
		node['left'] = node['right'] = to_terminal(left + right)
		return
	# check for max depth
	if depth >= max_depth:
		node['left'], node['right'] = to_terminal(left), to_terminal(right)
		return
	# process left child
	if len(left) <= min_size:
		node['left'] = to_terminal(left)
	else:
		node['left'] = get_split(left)
		split(node['left'], max_depth, min_size, depth+1)
	# process right child
	if len(right) <= min_size:
		node['right'] = to_terminal(right)
	else:
		node['right'] = get_split(right)
		split(node['right'], max_depth, min_size, depth+1)

# Build a decision tree
def build_tree(train, max_depth, min_size):
	root = get_split(train)
	split(root, max_depth, min_size, 1)
	return root

# Print a decision tree
def print_tree(node, depth=0):
	if isinstance(node, dict):
		print('%s[X%d < %.3f]' % ((depth*' ', (node['index']+1), node['value'])))
		print_tree(node['left'], depth+1)
		print_tree(node['right'], depth+1)
	else:
		print('%s[%s]' % ((depth*' ', node)))
        
# Make a prediction with a decision tree
def predict(node, row):
	if row[node['index']] < node['value']:
		if isinstance(node['left'], dict):
			return predict(node['left'], row)
		else:
			return node['left']
	else:
		if isinstance(node['right'], dict):
			return predict(node['right'], row)
		else:
			return node['right']

#dataset = [[358.0, 75.0, 44.0, 5.0, 0],
#    [353.0, 76.0, 44.0, 5.0, 0],
#    [362.0, 77.0, 45.0, 4.0, 0],
#    [344.0, 75.0, 44.0, 4.0, 0],
#    [323.0, 45.0, 45.0, 4.0, 1],
#    [401.0, 77.0, 44.0, 4.0, 0],
#    [421.0, 75.0, 43.0, 3.0, 0],
#    [322.0, 90.0, 44.0, 3.0, 1],
#    [345.0, 76.0, 46.0, 3.0, 0],
#    [367.0, 75.0, 46.0, 2.0, 1],
#    [288.0, 80.0, 47.0, 2.0, 1],
#    [349.0, 78.0, 48.0, 4.0, 0],
#    [399.0, 79.0, 49.0, 5.0, 0],
#    [364.0, 75.0, 50.0, 5.0, 0],
#    [231.0, 75.0, 44.0, 5.0, 1],
#    [221.0, 75.0, 44.0, 5.0, 1],
#    [114.0, 75.0, 44.0, 5.0, 1],
#    [302.0, 75.0, 44.0, 5.0, 0],
#    [364.0, 88.0, 44.0, 5.0, 1],
#    [367.0, 94.0, 44.0, 5.0, 1],
#    [382.0, 71.0, 41.0, 3.0, 0],
#    [376.0, 75.0, 44.0, 5.0, 0],
#    [654.0, 75.0, 44.0, 5.0, 1],
#    [754.0, 75.0, 44.0, 5.0, 1],
#    [801.0, 75.0, 44.0, 5.0, 1],
#    [367.0, 75.0, 44.0, 1.0, 0],
#    [358.0, 75.0, 44.0, 1.0, 0],
#    [356.0, 75.0, 14.0, 5.0, 1],
#    [355.0, 75.0, 24.0, 5.0, 1],
#    [352.0, 75.0, 44.0, 9.0, 1],
#    [348.0, 75.0, 44.0, 9.0, 1],
#    [299.0, 86.0, 81.0, 1.9, 1],
#    [299.0, 69.0, 39.0, 7.0, 1],
#    [400.0, 77.5, 60.0, 4.0, 0],
#    [400.0, 77.5, 60.0, 4.0, 0],
#    [400.0, 77.5, 60.0, 4.0, 0],
#    [400.0, 77.5, 60.0, 4.0, 0],
#    [400.0, 77.5, 60.0, 4.0, 0],
#    [400.0, 77.5, 60.0, 4.0, 0],
#    [400.0, 77.5, 60.0, 4.0, 0],
#    [400.0, 77.5, 60.0, 4.0, 0],
#    [400.0, 77.5, 60.0, 4.0, 0],
#    [400.0, 77.5, 60.0, 4.0, 0],
#    [501.0, 86.0, 81.0, 7.0, 1]]
dataset = [[299.0,-1],
    [300.0,+5],
    [400.0,+5],
    [600.0,+3]]
#TODO: Create a master list of data with proper classing

tree = build_tree(dataset, 2, 1)
print_tree(tree)
#  predict with a stump (finds water < value)
#TODO: Modify this slightly to be used for User-Mode (i.e. 'value's value needs changed to one set from xml)
stumpWL = {'index': 0, 'right': 0, 'value': 300.0, 'left': 1}
# finds water > value
stumpWG = {'index': 0, 'right': 1, 'value': 500.0, 'left': 0}
# finds temp < value
#stumpTL = {'index': 1, 'right': 0, 'value': 70.0, 'left': 1}
# finds temp > value
#stumpTG = {'index': 1, 'right': 1, 'value': 85.0, 'left': 0}
# finds humid < value
#stumpHL = {'index': 2, 'right': 0, 'value': 40.0, 'left': 1}
# finds humid > value
#stumpHG = {'index': 2, 'right': 1, 'value': 80.0, 'left': 0}
# finds light < value
#stumpLL = {'index': 3, 'right': 0, 'value': 2.0, 'left': 1}
# finds light > value
#stumpLG = {'index': 3, 'right': 1, 'value': 6.0, 'left': 0}


for i, row in enumerate(dataset):
    predictionWL = predict(stumpWL, row)
    #print "WL = " + str(predictionWL)
    predictionWG = predict(stumpWG, row)
    #print "WG = " + str(predictionWG)
    #predictionTL = predict(stumpTL, row)
    #print "TL = " + str(predictionTL)
    #predictionTG = predict(stumpTG, row)
    #print "TG = " + str(predictionTG)
    #predictionHL = predict(stumpHL, row)
    #print "HL = " + str(predictionHL)
    #predictionHG = predict(stumpHG, row)
    #print "HG = " + str(predictionHG)
    #predictionLL = predict(stumpLL, row)
    #print "LL = " + str(predictionLL)
    #predictionLG = predict(stumpLG, row)
    #print "LG = " + str(predictionLG)
    
    #print "Row = " + str(row)
    #Max = 8
    #Min = 0
    prediction = (predictionWL * 4) + (predictionWG * 2)# + (predictionTL * 1) + (predictionTG * 1) + (predictionHL * 1) + (predictionHG * 1) + (predictionLL * 2) + (predictionLG * 1)
    #print prediction
    if prediction >= 4:
        prediction = "-%d" %(int(1+(prediction-4)))
        print('Expected=%d, Got='% (row[-1]) + prediction)
    else:
        if prediction == 0:
            print('Expected=%d, Got=+'% (row[-1]) + str(prediction+5))
        else:
            prediction = "+%d" %(int(1+(4-prediction)))
            print('Expected=%d, Got='% (row[-1]) + prediction)

            
class statusThread (threading.Thread):
    def __init__(self, tID, pID, nextRun):
        threading.Thread.__init__(self)
        self.tID = tID
        self.pID = pID
        self.nextRun = nextRun
    def run(self):
        print "Starting Status Update Thread: " + self.tID
        #This builds the data for making a tree
        if self.nextRun != 0:
            plantList = []
            typeList = []
            for child in root.findall('activeplants'):
                for childp in child:
                    plantList.append(childp.attrib)
                    #Water List
                    #Temp List
                    #Humid List
                    #Light List
                    
            #print plantList
            for child in root.findall('planttype'):
                for childp in child:
                    typeList.append(childp.attrib)
            #print typeList
            #for attr in plantList:
                #print attr
        #print float(self.nextRun) 
        #This is in seconds
        time.sleep(float(self.nextRun)) 
        print "Ending Status Update Thread: " + self.tID
        nThread = statusThread("2", "-1", "100")
        del plantList, typeList
        nThread.start()
        return 0
        
#def getData:
    #Collect Data on all plants every so often
    #Compile the data into a list
        
#def orgData:
    #

#def statusPredict:

class webServThread (threading.Thread):
    def __init__ (self):
        threading.Thread.__init__(self)
    def run(self):
        app.run()
        
thread1 = statusThread("1", "-1", "100")
thread2 = webServThread()

thread1.start()
thread2.start()

#if __name__ == "__main__":
    #app.run()
    
