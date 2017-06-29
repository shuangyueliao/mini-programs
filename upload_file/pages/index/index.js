
Page({
  data:{
    imgSrc:'',
    mydata:'no',
    downloadImg:''
  },
  login: function () {
    
    wx.login({
      success: function (res) {
        console.log("code:"+res.code);
        if (res.code) {
          //发起网络请求
          wx.request({
            url: 'http://192.168.191.1:8080/SpringLearn/login',
            data: {
              code: res.code
            },
            success:function(res){
              console.log(res.data);
            }
          })
        } else {
          console.log('获取用户登录态失败！' + res.errMsg)
        }
      }
    });
  },
  myrequest:function(){
    var that=this;
    wx.downloadFile({
      url:'http://192.168.191.1:8080/SpringLearn/images/a.jpg',
      success:function(res){ 
        console.log(res);
        that.setData({
          mydata:'下载成功',
          downloadImg: res.tempFilePath
        })
      }
    })
    
  },
  uploadImg:function(){
    var that=this;
    wx.chooseImage({
      count: 1, // 默认9
      sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
      success: function (res) {
        // 返回选定照片的本地文件路径列表，tempFilePath可以作为img标签的src属性显示图片
        var tempFilePaths = res.tempFilePaths
        that.setData({
          imgSrc: tempFilePaths[0]
          
        });
        wx.uploadFile({
          url: 'http://192.168.191.1:8080/SpringLearn/upload', //仅为示例，非真实的接口地址
          filePath: tempFilePaths[0],
          name: 'file',
          formData: {
            'user': 'test'
          },
          success: function (res) {
            console.log(res);
            wx.showToast({
              title: 'success', 
            })
          }
        })
      }
    })
  }
})
