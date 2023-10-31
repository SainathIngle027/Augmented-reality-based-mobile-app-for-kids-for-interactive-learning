from azure.cognitiveservices.vision.computervision import ComputerVisionClient
from azure.cognitiveservices.vision.computervision.models import OperationStatusCodes
from azure.cognitiveservices.vision.computervision.models import VisualFeatureTypes ,  Details
from msrest.authentication import CognitiveServicesCredentials



def main(path):
    key = '0650b4d603f34622a6a3868e140df06f'
    endpoint = 'https://arleanixmodel27.cognitiveservices.azure.com/'
    computervision_client = ComputerVisionClient(endpoint, CognitiveServicesCredentials(key))

    remote_image_url = path

    # Print results with confidence score
    tags_result_remote = computervision_client.tag_image(remote_image_url )

    #Object name in array
    object = ["apple", "banana", "car" , "chair" ,"cat","horse" ,"dog","eagle", "fish","deer", "goat","elephant", "owl", "crow", "bat", "hen", "ball", "giraffe", "kangaroo",
              "lion", "mango", "monkey", "nutria", "orange", "parrot", "pineapple", "queen", "rabbit", "rat", "peacock ", "swans", "tiger"]
    flag = 0
    store = ""
    count = 0
    result = ""

    # Print results with confidence score
    if (len(tags_result_remote.tags) == 0):
        flag = 1
        return "No object found."
    else:
        for tag in tags_result_remote.tags:
            count = count + 1
            if(count==4):
                store =tag.name
            for obj in object:
                if(tag.name==obj):
                    flag = 1
                    return obj
    if(flag==0):
        return store