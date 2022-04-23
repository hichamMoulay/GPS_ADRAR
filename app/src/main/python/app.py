import joblib
import json5
import numpy as np
import os.path


def predictDistance(latitude_a, longitude_a, latitude_b, longitude_b, number_day, period_time, distance):

    #model = pickle.load(open('distance.pkl', 'rb'))

    filename = os.path.join(os.path.dirname(__file__), "speed.obj")
    print("filename = ",filename)
    model = joblib.load(open(filename, "rb"))

    input_query = np.array([[latitude_a, longitude_a, latitude_b, longitude_b, number_day, period_time, distance]])
    result = model.predict(input_query)[0]

    result = '{"result_Distance" :' + str(result) + '}'
    resultJSON=json5.loads(result)
    print(resultJSON)

    return resultJSON

def predictTime(latitude_a, longitude_a, latitude_b, longitude_b, number_day, period_time, distance):

    #model = pickle.load(open('time.pkl', 'rb'))

    filename = os.path.join(os.path.dirname(__file__), "time.obj")
    model = joblib.load(open(filename, "rb"))

    input_query = np.array([[latitude_a, longitude_a, latitude_b, longitude_b, number_day, period_time, distance]])
    result = model.predict(input_query)[0]

    result = '{"result_time" :' + str(result) + '}'
    resultJSON=json5.loads(result)
    print(resultJSON)

    return resultJSON

def version():
    print("np ",np.version.version)
    print("joblib ",joblib.__version__)
    print("json ",json5.VERSION)

if __name__ == '__main__':
    print("run ok")
    #predictDistance(27.8829680268151,-0.28564667934856,27.8829668014029,-0.285645876144924,7,3,0.92987615457897)
    #predictTime(27.8829680268151,-0.28564667934856,27.8829668014029,-0.285645876144924,7,3,0.92987615457897)
    #version()
    #predict()
