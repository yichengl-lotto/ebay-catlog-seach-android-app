
var express = require('express')
var cors = require('cors')
var app = express()
//const https = require('https')
const request = require('request');

//var url = 'https://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=YichengL-CSCI571H-PRD-22eba4255-b407d78d&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&keywords=iphone&paginationInput.entriesPerPage=10&sortOrder=BestMatch&itemFilter(0).name=MaxPrice&itemFilter(0).value=25&itemFilter(0).paramName=Currency&itemFilter(0).paramValue=USD'
var url1 = 'https://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=YichengL-CSCI571H-PRD-22eba4255-b407d78d&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&paginationInput.entriesPerPage=50';
var url2 = 'http://open.api.ebay.com/shopping?callname=GetSingleItem&responseencoding=JSON&appid=YichengL-CSCI571H-PRD-22eba4255-b407d78d&siteid=0&version=967&IncludeSelector=Description,Details,ItemSpecifics&ItemID=';
var url = '';
var filter_num = 0;
var value_num = 0;

app.use(cors())
app.use(function (req, res, next) {

    // Website you wish to allow to connect
    res.setHeader('Access-Control-Allow-Origin', '*');

    // Request methods you wish to allow
    res.setHeader('Access-Control-Allow-Methods', 'GET, PUT');

    // Request headers you wish to allow
    res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type');

    // Set to true if you need the website to include cookies in the requests sent
    // to the API (e.g. in case you use sessions)
    res.setHeader('Access-Control-Allow-Credentials', true);

    // Pass to next layer of middleware
    next();
});

//app.use(app.router);

/*
request.get(url,{json:true},(err,res,body) => {
   window.myresult = body;
   console.log(body)
});
*/
app.get('/',function(req,response){
  if (req.query.urltype == "findItemsAdvanced") {
    url = url1;
    if( req.query.sortby !="") {
      url+="&sortOrder="+req.query.sortby;
      console.log("sort Oder: ", req.query.sortby);
    }
    if ( req.query.keyword != "") {
      url += "&keywords=".concat(req.query.keyword)
      console.log("keyword: ",req.query.keyword);
    }

    if( typeof req.query.maxprice !== 'undefined') {
      url+= "&itemFilter("+ filter_num+").name=MaxPrice&itemFilter("+filter_num+").value="+req.query.maxprice+"&itemFilter("+filter_num+").paramName=Currency&itemFilter("+filter_num+").paramValue=USD";
      filter_num += 1;
      console.log("maxprice: ", req.query.maxprice);
    }
    if( typeof req.query.minprice !== 'undefined') {
      url+= "&itemFilter("+ filter_num+").name=MinPrice&itemFilter("+filter_num+").value="+req.query.minprice+"&itemFilter("+filter_num+").paramName=Currency&itemFilter("+filter_num+").paramValue=USD";
      filter_num += 1;
      console.log("minprice: ", req.query.minprice);
    }

    /*if( req.query.returnOnly !="") {
      url += "&itemFilter("+filter_num+").name=ReturnsAcceptedOnly&itemFilter("+filter_num+").value="+req.query.returnOnly;
      filter_num +=1;
      console.log("returnOnly: ", req.query.returnOnly);
    }*/
    /*
    if( req.query.freeshipOnly !="") {
      url += "&itemFilter("+filter_num+").name=FreeShippingOnly&itemFilter("+filter_num+").value="+req.query.freeshipOnly;

      filter_num+=1;
      console.log(url);
      console.log("freeshipOnly: ",req.query.freeshipOnly);
    }*/
    /*
    if(req.query.expship =="true") {
      url +="&itemFilter("+filter_num+").name=ExpeditedShippingType&itemFilter("+filter_num+").value=Expedited";
      filter_num+=1;
      console.log("exship: ", req.query.expship);
    }*/
    if (req.query.condition_new == "New" ||  req.query.condition_used == "Used" || req.query.condition_unspecified =="Unspecified"){
      url += "&itemFilter("+filter_num+").name=Condition";
      if (req.query.condition_new =="New") {
        url += "&itemFilter("+filter_num+").value("+value_num+")=New";
        value_num+=1;
      }


      if (req.query.condition_used =="Used") {
        url += "&itemFilter("+filter_num+").value("+value_num+")=Used";
        value_num+=1;
      }


      if (req.query.condition_unspecified=="Unspecified") {
        url += "&itemFilter("+filter_num+").value("+value_num+")=Unspecified";
        value_num+=1;
      }

    }

    //url1 = 'https://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=YichengL-CSCI571H-PRD-22eba4255-b407d78d&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD';

  }
  if(req.query.urltype == "GetSingleItem") {
    url = url2;
    url += req.query.itemID;
    console.log(url);
  }


  console.log("filter_num: ",filter_num);
  console.log("url: ", url);
  request.get(url,{json:true},(err,res,body) => {
     response.json(body);

  });
  filter_num = 0;
  value_num = 0;
  url = '';

});
/*
var getCall = function(request,response) {
  https.get('https://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=YichengL-CSCI571H-PRD-22eba4255-b407d78d&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&keywords=iphone&paginationInput.entriesPerPage=10&sortOrder=BestMatch&itemFilter(0).name=MaxPrice&itemFilter(0).value=25&itemFilter(0).paramName=Currency&itemFilter(0).paramValue=USD',function(res) {
    res.on("data",function(d){
      response.json(JSON.parse(d));
    });
    res.on("error",function(e) {
      response.json(res.statusCode,Json.parse(e))
    });
  });
}
*/
//app.get('/',getCall);

app.listen(8080,() => {
  console.log("server is listening")
})


//https.get('https://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=YichengL-CSCI571H-PRD-22eba4255-b407d78d&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&keywords=iphone&paginationInput.entriesPerPage=10&sortOrder=BestMatch&itemFilter(0).name=MaxPrice&itemFilter(0).value=25&itemFilter(0).paramName=Currency&itemFilter(0).paramValue=USD',(req,resp) => {
//  let data ='';
//  resp.on('data',(chunk) => {
//    data += chunk;
//  });
//  resp.on('end',()=> {
//    let obj = JSON.parse(data);
//    resp.send(obj);
//  });
//}).on('error',(err) => {
//  res.send('error: '+err.message);
//});
