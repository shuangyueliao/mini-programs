// pages/index1/index1.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    message:''
  },
  connect:function(){
    var that=this;
    wx.connectSocket({
      url: 'ws://192.168.191.1:8080/HelloWorld/echo',
     
    });
    wx.onSocketOpen(function (res) {
      console.log('WebSocket连接已打开！')
      wx.onSocketMessage(function(res){
        that.setData({
          message:res.data
        })
      })
    })
    wx.onSocketError(function (res) {
      console.log('WebSocket连接打开失败，请检查！')
    })
    
  },
  
  send:function(e){
    wx.sendSocketMessage({
      data: e.detail.value.hi,
    })
  },
  mysubmit:function(e){
    console.log(e)
    wx.request({
      url: 'http://110.65.97.47:8080/SpringLearn/templateMessage',
      data:{
        form_id:e.detail.formId,
        thing:e.detail.value.thing,
        price:e.detail.value.price
      },
      header:{
        'content-type': "application/x-www-form-urlencoded"
      },
      method:"POST", 
      success:function(res){
        console.log(res.data);
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
  
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