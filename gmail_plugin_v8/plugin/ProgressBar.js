// JavaScript Document
 function LoadingBar(id)
        {
            this.loadbar = $("#" + id);
            this.percentEle = $(".percent", this.loadbar);
            this.percentNumEle = $(".percentNum", this.loadbar);
            this.max = 100;
            this.currentProgress = 0;
        }
        LoadingBar.prototype = {
            constructor: LoadingBar,
            setMax: function (maxVal)
            {
                this.max = maxVal;
            },
            setProgress: function (val)
            {
                if (val >= this.max)
                {
                    val = this.max;
                }
                this.currentProgress = parseInt((val / this.max) * 100) + "%";
                this.percentEle.width(this.currentProgress);
                this.percentNumEle.text(this.currentProgress);


            }
        };
   $(function ()
        {

            var loadbar = new LoadingBar("loadBar01");
            var max = 1000;
            loadbar.setMax(max);
            var i = 0;
            var time = setInterval(function ()
            {
                loadbar.setProgress(i);
                if (i == max)
                {
                    clearInterval(time);
                    return;
                }
                i += 10;
            }, 40);
        });