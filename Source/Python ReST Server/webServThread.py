import web
import threading
import time
import datetime
import xml.etree.cElementTree as ET

tree = ET.parse('plant_data.xml')
root = tree.getroot()
prevPixyL = []
prevCount = 0

urls = (
    #This is standard request format: /plants
    '/plants', 'all_plants',
    #This is standard request format: /plant/id#
    '/plant/(.*)', 'get_plant',
    #This is standard request format: /current
    '/current', 'cur_plant',
    #This is standard request format: /plantcount
    '/plantcount', 'plant_count',
    #This is standard request format: /add/plant/name lightSens waterSens humidSens tempSens health waterTimer foliageColor 
    #                            E.G: host:8080/add/plant/Bean1 4 315 45 78 100 4560 5 
    '/add/plant/(.*)', 'add_plant',
    #This is standard request format: /update/id lightS waterS humidS tempS health inTraining
    #                            E.G: host:8080/update/plant/7 4 315 45 78 100 1
    '/update/plant/(.*)', 'update_plant',
    #This is standard request format: /update/current/id lightS waterS humidS tempS health inTraining
    #                            E.G: host:8080/update/current/7 4 315 45 78 100 1
    '/update/current/(.*)', 'update_current',
    #This is standard request format: /update/foliage/id val
    #                            E.G: host:8080/update/foliage/2 4
    '/update/foliage/(.*)', 'update_foliage',
    #This is standard request format: /update/waterTimer/id val
    #                            E.G: host:8080/update/waterTimer/2 400
    '/update/waterTimer/(.*)', 'update_waterTimer',
    #This is standard request format: /remove/plant/id#
    '/remove/plant/(.*)', 'remove_plant',
    #This is standard request format: /current/userscore
    '/current/userscore', 'cur_uscore',
    #This is standard request format: /current/userscore/#
    #User Scores can be as follows: 0=Fine, 1=Needs Water, 2=Needs Light, 3=Bad Temp, 4=Bad Humid
    '/current/userscore/(.*)', 'change_uscore',
    #This is standard request format: /settings
    '/settings', 'sett_report',
    #This is standard request format: /settings/set/activateWaterFlag activateLightFlag sendTempNotificationFlag sendHumidNotificationFlag waitFlag
    #                            E.G: /settings/set/1 1 1 1 0
    '/settings/set/(.*)', 'sett_set',
    #This is standard request format: /settings/waiting
    '/settings/waiting', 'sett_wait',
    #This is standard request format: /settings/waiting/[0(off) or 1(on)]
    '/settings/waiting/(.*)', 'sett_waitset'
    #This is standard request format: /pixy/currentBlocks
    '/pixy/currentBlocks', 'pixy_blocks'
    #This is standard request format: /pixy/currentBlocks/set/
    '/pixy/currentBlocks/set/(.*)', 'pixy_set'
)

app = web.application(urls, globals())

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
            
class plant_count:
    def GET(self):
        print 'Plant count'
        for child in root.findall('activeplants'):
            return len(child.findall('plant'))

class add_plant:
    def GET(self, data):
        print 'Add plant'
        for child in root.findall('activeplants'):
            npID = int(child.attrib['id'])
            data = map(str, data.split())
            nRoot = ET.SubElement(child, 'plant', {'id':str(npID), 'name':str(data[0]), 'lightSens':str(data[1]), 'waterSens':str(data[2]), 'humidSens':str(data[3]), 'tempSens':str(data[4]), 'health':str(data[5]), 'waterTimer':str(data[6]), 'foliageColor':str(data[7]),'inTraining':'1'})
            for childp in root.findall('dtrees'):
                nRoot2 = ET.SubElement(childp, 'tree', {'plantid':str(npID), 't':'[]'})
            child.set('id', str(npID+1))
            #ET.dump(root)
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
                    childp.set('lightSens', str(plant[1]))
                    childp.set('waterSens', str(plant[2]))
                    childp.set('humidSens', str(plant[3]))
                    childp.set('tempSens', str(plant[4]))
                    childp.set('health', str(plant[5]))
                    childp.set('inTraining', str(plant[6]))
                    tree = ET.ElementTree(root)
                    tree.write('plant_data.xml')
                    return 'Success'
        return 'Failure'

class update_foliage:
    def GET(self, data):
        print 'Update foliage'
        plant = map(int, data.split())
        for child in root.findall('activeplants'):
            for childp in child:
                if childp.attrib['id'] == str(plant[0]):
                    childp.set('foliageColor', str(plant[1]))
                    tree = ET.ElementTree(root)
                    tree.write('plant_data.xml')
                    return 'Success'
        return 'Failure'
        
class update_waterTimer:
    def GET(self, data):
        print 'Update waterTimer'
        plant = map(int, data.split())
        for child in root.findall('activeplants'):
            for childp in child:
                if childp.attrib['id'] == str(plant[0]):
                    childp.set('waterTimer', str(plant[1]))
                    tree = ET.ElementTree(root)
                    tree.write('plant_data.xml')
                    return 'Success'
        return 'Failure'
        
class update_current:
    def GET(self, data):
        print 'Update cur plant'
        plant = map(int, data.split())
        for child in root.findall('curplant'):
            child.set('id', str(plant[0]))
            child.set('lightSens', str(plant[1]))
            child.set('waterSens', str(plant[2]))
            child.set('humidSens', str(plant[3]))
            child.set('tempSens', str(plant[4]))
            child.set('health', str(plant[5]))
            child.set('inTraining', str(plant[6]))
        for child in root.findall('activeplants'):
            for childp in child:
                if childp.attrib['id'] == str(plant[0]):
                    childp.set('lightSens', str(plant[1]))
                    childp.set('waterSens', str(plant[2]))
                    childp.set('humidSens', str(plant[3]))
                    childp.set('tempSens', str(plant[4]))
                    childp.set('health', str(plant[5]))
                    childp.set('inTraining', str(plant[6]))
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
            
class cur_uscore:
    def GET(self):
        print 'cur userscore'
        for child in root.findall('userscore'):
            return str(child.attrib['score'])

class change_uscore:
    def GET(self, score):
        print 'change userscore'
        for child in root.findall('userscore'):
            child.set('score',score)
            tree = ET.ElementTree(root)
            tree.write('plant_data.xml')
            return 'Success'
        return 'Failure'
        
class sett_report:
    def GET(self):
        print 'settings report'
        for child in root.findall('settings'):
            return str(child.attrib)
            
class sett_set:
    def GET(self, data):
        print 'set settings'
        sett = map(int, data.split())
        for child in root.findall('settings'):
            child.set('waterFlag', str(sett[0]))
            child.set('lightFlag', str(sett[1]))
            child.set('tempFlag', str(sett[2]))
            child.set('humidFlag', str(sett[3]))
            child.set('waitFlag', str(sett[4]))
            tree = ET.ElementTree(root)
            tree.write('plant_data.xml')
            return 'Success'
        return 'Failure'
        
class sett_wait:
    def GET(self):
        print 'current waiting setting'
        for child in root.findall('waiting'):
            return str(child.attrib['status'])
            
class sett_waitset:
    def GET(self, wait):
        print 'change waiting setting'
        for child in root.findall('waiting'):
            child.set('status',str(wait))
            tree = ET.ElementTree(root)
            tree.write('plant_data.xml')
            return 'Success'
        return 'Failure'
        
        
class pixy_blocks:
    def GET(self):
        print 'pixy blocks'
        for child in root.findall('pixy'):
            return str(child.attrib)
            
class pixy_set:
    def GET(self, pixy):
        print 'set pixy'
        for child in root.findall('pixy'):
            child.attrib['prevPixy'] = child.attrib['data']
            child.set('data',str(pixy))
            tree = ET.ElementTree(root)
            tree.write('plant_data.xml')
            return 'Success'
        return 'Failure'


###############################
#
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
            
#########################################################




#dataset = [[358.0, 75.0, 44.0, 5.0, 0],
#    [353.0, 76.0, 44.0, 5.0, 0],
#    [362.0, 77.0, 45.0, 4.0, 0],
#    [344.0, 75.0, 44.0, 4.0, 0],
#    [323.0, 75.0, 45.0, 4.0, 0],
#    [401.0, 77.0, 44.0, 4.0, 0],
#    [421.0, 75.0, 43.0, 3.0, 0],
#    [322.0, 77.0, 44.0, 3.0, 0],
#    [345.0, 76.0, 46.0, 3.0, 0],
#    [367.0, 75.0, 46.0, 2.0, 0],
#    [288.0, 80.0, 47.0, 2.0, 1],
#    [349.0, 78.0, 48.0, 5.0, 0],
#    [399.0, 79.0, 49.0, 5.0, 0],
#    [364.0, 75.0, 50.0, 0.0, 2],
#    [221.0, 75.0, 44.0, 0.0, 1],
#    [114.0, 75.0, 44.0, 0.0, 1],
#    [332.0, 75.0, 44.0, 0.0, 2],
#    [342.0, 75.0, 44.0, 0.0, 2],
#    [322.0, 75.0, 44.0, 0.0, 2],
#    [364.0, 88.0, 44.0, 6.0, 3],
#    [367.0, 94.0, 44.0, 6.0, 3],
#    [382.0, 71.0, 41.0, 5.0, 0],
#    [376.0, 75.0, 44.0, 2.0, 0],
#    [367.0, 75.0, 44.0, 2.0, 0],
#    [358.0, 75.0, 44.0, 3.0, 0],
#    [356.0, 75.0, 14.0, 5.0, 4],
#    [355.0, 75.0, 24.0, 5.0, 4],
#    [352.0, 75.0, 44.0, 6.0, 0],
#    [348.0, 75.0, 44.0, 6.0, 0]]                 

#dtree = build_tree(dataset, 4, 6)
#print_tree(dtree)
#
#[X1 < 322.000]
# [1]
# [X4 < 2.000]
#  [2]
#  [X2 < 88.000]
#   [X3 < 41.000]
#    [4]
#    [0]
#   [3]
#####
# Save and recall tree datasets
#for child in root.findall('dtrees'):
#    nRoot = ET.SubElement(child,'tree',{'t'=str(dataset),'plantid'='test'})
#    ET.dump(root)
#    tree.write('plant_data.xml') 
#for child in root.findall('dtrees'):
#    for childp in child:
#        if childp.attrib['plantid'] == 'test': 
#            treeDataset = childp.attrib['t']
#            temp = treeDataset.replace('[','').split('],')
#            treeDataset = [map(float, entry.replace(']','').split(',')) for entry in temp] 
#            print treeDataset           
#for i, row in enumerate(dataset):
#    prediction = predict(dtree, row)
#    print('Expected=%d, Got=%d' % (row[-1], prediction))
            
class statusThread (threading.Thread):
    def __init__(self, tID, count, prevL, nextRun):
        threading.Thread.__init__(self)
        self.tID = tID
        self.count = count
        self.prevL = prevL
        self.nextRun = nextRun
    def run(self):
        print "Starting Status Update Thread: " + self.tID
        #This builds the data for making a tree
        if self.nextRun != 0:
            plantList = []
            readingList = [0.0,0.0,0.0,0.0] 
            for child in root.findall('curplant'):
                for childp in root.findall('dtrees'):
                    for childp2 in childp:
                        if int(childp2.attrib['plantid']) == int(child.attrib['id']):
                            if childp2.attrib['t'] != "[]":
                                plantList = childp2.attrib['t']
                                temp = plantList.replace('[','').split('],')
                                plantList = [map(float, entry.replace(']','').split(',')) for entry in temp]
                                curTree = childp2
                readingList[0] = float(child.attrib['waterSens'])
                readingList[1] = float(child.attrib['tempSens'])
                readingList[2] = float(child.attrib['humidSens'])
                readingList[3] = float(child.attrib['lightSens'])
                if int(child.attrib['inTraining']) == 1:
                    if str(readingList) == str(self.prevL):
                        self.count = self.count + 1 
                        self.prevL = readingList
                    else:
                        self.count = 0
                        self.prevL = readingList
                    if self.count < 6:
                        timeH = datetime.datetime.now().hour
                        if timeH > 7 and timeH < 18:# 0==0:                    
                            for childp3 in root.findall('userscore'): 
                                readingList.append(float(childp3.attrib['score']))
                            if len(plantList) < 200:
                                plantList.append(readingList)
                                curTree.attrib['t'] = str(plantList)
                                #ET.dump(root)
                                tree.write('plant_data.xml')
                                readingList.pop(-1)
                            else:
                                plantList.pop(0)
                                plantList.append(readingList)
                                curTree.attrib['t'] = str(plantList)
                                #ET.dump(root)
                                tree.write('plant_data.xml')
                                readingList.pop(-1)
                else:
                    dtree = build_tree(plantList, 4, 6)
                    print_tree(dtree)
                    prediction = predict(dtree, readingList)
                    print prediction
                    for profile in root.findall('settings'):
                        #Elifs for classifications
                        if float(prediction) == 0.0:
                            profile.attrib['waterFlag'] = '0'
                            profile.attrib['lightFlag'] = '0'
                            profile.attrib['tempFlag'] = '0'
                            profile.attrib['humidFlag'] = '0'
                            tree.write('plant_data.xml')
                        elif float(prediction) == 1.0:
                            profile.attrib['waterFlag'] = '1'
                            profile.attrib['lightFlag'] = '0'
                            profile.attrib['tempFlag'] = '0'
                            profile.attrib['humidFlag'] = '0'
                            tree.write('plant_data.xml')
                        elif float(prediction) == 2.0:
                            profile.attrib['waterFlag'] = '0'
                            profile.attrib['lightFlag'] = '1'
                            profile.attrib['tempFlag'] = '0'
                            profile.attrib['humidFlag'] = '0'
                            tree.write('plant_data.xml')
                        elif float(prediction) == 3.0:
                            profile.attrib['waterFlag'] = '0'
                            profile.attrib['lightFlag'] = '0'
                            profile.attrib['tempFlag'] = '1'
                            profile.attrib['humidFlag'] = '0' 
                            tree.write('plant_data.xml')                            
                        elif float(prediction) == 4.0:
                            profile.attrib['waterFlag'] = '0'
                            profile.attrib['lightFlag'] = '0'
                            profile.attrib['tempFlag'] = '0'
                            profile.attrib['humidFlag'] = '1'
                            tree.write('plant_data.xml')                            
                        else:
                            print 'An Error has Occurred'
        #print float(self.nextRun) 
        #This is in seconds
        self.prevL = readingList
        #print "happening?"
        pixyScore()
        time.sleep(float(self.nextRun)) 
        self.nextRun = 0
        print "Ending Status Update Thread: " + self.tID
        nThread = statusThread(str(int(self.tID)+1), self.count, self.prevL, "50")
        del readingList
        nThread.start()
        return 0
        
def pixyScore():
    dataL = [] 
    prevDataL = []
    datax = 0; datay = 0; pdatax = 0; pdatay = 0
    global prevPixyL
    global prevCount
    #print "Here1"
    for child in root.findall('pixy'):
        data = child.attrib['data']
        prevData = child.attrib['prevPixy']
    for cur in root.findall('curplant'):
        print child.attrib['id'], cur.attrib['id']
        if int(child.attrib['id']) != int(cur.attrib['id']):
            for pixyR in root.findall('pixy'):
                pixyR.set('data', '1 11')
                pixyR.set('prevPixy', '1 11')
                pixyR.set('id',cur.attrib['id'])
    dataL = data.split(',')
    prevDataL = prevData.split(',')
    print prevPixyL, [dataL, prevDataL]
    if prevPixyL == [dataL, prevDataL]:
        prevCount = prevCount + 1
    else:
        prevCount = 0
    prevPixyL = [dataL, prevDataL]
    if prevCount < 4:
        for i,item in enumerate(dataL):
            if i == 0:
                score = int(data[0]) - int(prevData[0])
            else:
                datax = datax + int(item[0])
                datay = datay + int(item[1])
        for i,item in enumerate(prevDataL):
            if i != 0:
                pdatax = pdatax + int(item[0])
                pdatay = pdatay + int(item[1])
        if ((datax+datay)-(pdatax+pdatay)) > 0:
            score = score + 1
        elif ((datax+datay)-(pdatax+pdatay)) < 0:
            score = score - 1
        cur.attrib['health'] = str(int(cur.attrib['health']) + score)
        if int(cur.attrib['health']) < 0:
            cur.attrib['health'] = '0'
        elif int(cur.attrib['health']) > 100:
            cur.attrib['health'] = '100'
        #ET.dump(root)
        tree.write('plant_data.xml')
    
class webServThread (threading.Thread):
    def __init__ (self):
        threading.Thread.__init__(self)
    def run(self):    
        app.run()
        

if __name__ == "__main__":
    #app.run()
    thread1 = webServThread()
    thread1.start()
    thread2 = statusThread("1", 0, [], "50")
    thread2.start()

