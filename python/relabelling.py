# -*- coding: utf-8 -*-
"""
Created on Sat May 29 09:13:31 2021

@author: Jy
"""
import glob
import xml.etree.ElementTree as elemTree

classes = ['stone']

def convert_annotation(annotation_voc, image_id):
  w = 384
  h = 216
  tree = elemTree.parse(annotation_voc)
  root = tree.getroot()
  annotation_file = open(f'./label/{image_id}.txt', 'w')

  for obj in root.iter('object'):
    difficult = obj.find('difficult').text
    clss = obj.find('name').text
    if clss not in classes or int(difficult) == 1: 
        continue
    cls_id = classes.index(clss)
    xmlbox = obj.find('bndbox')
    b = (int(xmlbox.find('xmin').text), int(xmlbox.find('ymin').text), int(xmlbox.find('xmax').text), int(xmlbox.find('ymax').text))
    print(b)
  bb = convert_format((w,h),b)
  annotation_file.write(str(cls_id) + ' ' + ' '.join([str(a) for a in bb]) + ' ')
  annotation_file.close()

def convert_format(size, box):
    dw = 1./size[0]
    dh = 1./size[1]
    x = (box[0] + box[2])/2.0
    y = (box[1] + box[3])/2.0
    w = box[2] - box[0]
    h = box[3] - box[1]
    x = x*dw
    w = w*dw
    y = y*dh
    h = h*dh
    return (x,y,w,h)

for className in classes:
    annotations_voc = glob.glob('./stone/*.xml')
    for annotation_voc in annotations_voc:
        xmlPath = annotation_voc.split('.')
        imgIdx = xmlPath[-2]
        image_id = imgIdx.split('\\')
        print(image_id[-1])
        convert_annotation(annotation_voc, image_id[-1])