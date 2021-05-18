# -*- coding: utf-8 -*-
"""
Created on Thu May 13 12:00:33 2021

@author: Jy
"""
import glob
import cv2
from PIL import Image
import xml.etree.ElementTree as elemTree

classes = ['stone']

def split_video():
    videoFiles = './20210430_120955.mp4' #0~296
    #videoFiles = './20210509_173557.mp4' #297~694
    #videoFiles = './20210511_162322.mp4' #695~1007
    #videoFiles = './20210511_165004.mp4' #1008~1361

    for i in range(len(videoFiles)):
        cam = cv2.VideoCapture(videoFiles)
        currentFrame = 0
        while(True):
            ret, frame = cam.read()
            if ret:
                cv2.imwrite(str(currentFrame) + '.jpg', frame)
                currentFrame += 1
            else:
                break
    
        cam.release()


def resize():
    newPath = './stone/'
    #이미지 폴더 열고 1/20으로 resize

    images = glob.glob('./*.jpg')
    for image_file in images:
      loc = image_file.split('/')
      image = Image.open(image_file)
      resize_image = image.resize((384,216))
      resize_image.save(newPath+loc[-1])
      print(loc[-1])
    #boundig box 변경
 
    xmls = glob.glob('./*.xml')

    for xml in xmls:
      xmlPath = xml.split('.')
      imgIdx = xmlPath[-2]
      tree = elemTree.parse(xml)
      #경로 수정
      folder = tree.find('./folder')
      folder.text = 'stone'
      path = tree.find('./path')
      path.text = '/stone/'+imgIdx[1:]+'.JPG'
    
      # bounding box 수정
      objects = tree.findall('./object')
      for i, object_ in enumerate(objects):
        bndbox = object_.find('./bndbox')
        
        xmin = int(bndbox.find('./xmin').text)//10
        bndbox.find('./xmin').text = str(xmin)
        ymin = int(bndbox.find('./ymin').text)//10
        bndbox.find('./ymin').text = str(ymin)
        xmax = int(bndbox.find('./xmax').text)//10
        bndbox.find('./xmax').text = str(xmax)
        ymax = int(bndbox.find('./ymax').text)//10
        bndbox.find('./ymax').text = str(ymax)
      tree.write(newPath+imgIdx[1:]+'.xml' , encoding='utf8')
      print(imgIdx[1:])

def convert_annotation(annotation_voc, train_all_file):
  tree = elemTree.parse(annotation_voc)
  root = tree.getroot()

  for obj in root.iter('object'):
    difficult = obj.find('difficult').text
    clss = obj.find('name').text
    if clss not in classes or int(difficult) == 1: 
        continue
    cls_id = classes.index(clss)
    xmlbox = obj.find('bndbox')
    b = (int(xmlbox.find('xmin').text), int(xmlbox.find('ymin').text), int(xmlbox.find('xmax').text), int(xmlbox.find('ymax').text))
    train_all_file.write(" " + ",".join([str(a) for a in b]) + ',' + str(cls_id))

split_video()
resize()
train_all_file = open('./train_all.txt', 'w')

for className in classes:
    annotations_voc = glob.glob('./*.xml')
    for annotation_voc in annotations_voc:
        xmlPath = annotation_voc.split('.')
        imgIdx = xmlPath[-2]
        image_id = imgIdx[1:]+'.jpg'
        train_all_file.write(f'/content/drive/MyDrive/img_set/{className}/{image_id}')
        convert_annotation(annotation_voc, train_all_file)
        train_all_file.write('\n')
train_all_file.close()