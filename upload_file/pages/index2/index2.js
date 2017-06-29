// pages/index2/index2.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    session_key:''
  },
  test:function(){
   
    wx.request({
      url: 'http://110.65.97.47:8080/SpringLearn/hello1',
      success:function(res){
        console.log(res.data)
      }
    })
  },
  get:function(){
    var that=this;
    wx.getUserInfo({
      success: function (res) {
        console.log(res)
        var userInfo = res.userInfo
        var nickName = userInfo.nickName
        var avatarUrl = userInfo.avatarUrl
        var gender = userInfo.gender //性别 0：未知、1：男、2：女
        var province = userInfo.province
        var city = userInfo.city
        var country = userInfo.country

        wx.request({
          url: 'http://192.168.191.1:8080/SpringLearn/get',
          data: {
            encryptedData: res.encryptedData,
            iv:res.iv,
            sessionKey: that.data.session_key,
          },
          success: function (res) {
            console.log(res.data);
          }
        })
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that=this;
    wx.login({
      success: function (res) {
        console.log("code:" + res.code);
        if (res.code) {
          //发起网络请求
          wx.request({
            url: 'http://192.168.191.1:8080/SpringLearn/login',
            data: {
              code: res.code
            },
            success: function (res) {
              console.log(res.data);
              that.setData({
                session_key: res.data.session_key
              })
            }
          })
        } else {
          console.log('获取用户登录态失败！' + res.errMsg)
        }
      }
    });
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
  
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
  
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
  
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
  
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
  
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
  
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
  
  }
})