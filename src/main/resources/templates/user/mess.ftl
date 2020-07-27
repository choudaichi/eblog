<#include "/inc/layout.ftl" />

<@layout "用户中心">
    <div class="layui-container fly-marginTop fly-user-main">
        <@centerLeft level=3></@centerLeft>

        <div class="site-tree-mobile layui-hide">
            <i class="layui-icon">&#xe602;</i>
        </div>
        <div class="site-mobile-shade"></div>

        <div class="site-tree-mobile layui-hide">
            <i class="layui-icon">&#xe602;</i>
        </div>
        <div class="site-mobile-shade"></div>


        <div class="fly-panel fly-panel-user" pad20>
            <div class="layui-tab layui-tab-brief" lay-filter="user" id="LAY_msg" style="margin-top: 15px;">
                <button class="layui-btn layui-btn-danger" id="LAY_delallmsg">清空全部消息</button>
                <div id="LAY_minemsg" style="margin-top: 10px;">
                    <!--<div class="fly-none">您暂时没有最新消息</div>-->
                    <ul class="mine-msg">
                        <#list pageData.records as mess>

                            <li data-id="${mess.id}">
                                <blockquote class="layui-elem-quote">
                                    <#if mess.type == 0>
                                        系统消息：${mess.content}
                                    </#if>
                                    <#if mess.type == 1>
                                        <a href="/user/home/${mess.fromUserId}"
                                           target="_blank"><cite> ${mess.fromUserName} </cite></a> 评论了你的文章 <a
                                            target="_blank" href="/post/${mess.postId}"><cite> ${mess.postTitle} </cite></a>，内容是 (
                                        <a target="_blank" href="/post/${mess.postId}#comment"><cite
                                                    class="my-content">${mess.content}</cite></a>)
                                    </#if>
                                    <#if mess.type == 2>
                                        <a href="/user/home/${mess.fromUserId}"
                                           target="_blank"><cite> ${mess.fromUserName} </cite></a> 回复了你的评论 (<a
                                            target="_blank" href="/post/${mess.postId}#comment"><cite
                                                class="my-content">${mess.content}</cite></a>)，文章是 <a
                                            target="_blank" href="/post/${mess.postId}"><cite>${mess.postTitle} </cite></a>
                                    </#if>

                                </blockquote>
                                <p><span>${timeAgo(mess.creted)}</span><a href="javascript:;"
                                                                          class="layui-btn layui-btn-small layui-btn-danger fly-delete">删除</a>
                                </p>
                            </li>
                        </#list>
                    </ul>

                    <@paging pageData></@paging>

                </div>
            </div>
        </div>

    </div>

    <script>
        layui.cache.page = 'user';
        $(function () {
            layui.use(['fly', 'face'], function () {
                var fly = layui.fly;
                $('.my-content').each(function () {
                    var othis = $(this), html = othis.html();
                    othis.html(fly.content(html));
                });
            });
        });
    </script>

</@layout>