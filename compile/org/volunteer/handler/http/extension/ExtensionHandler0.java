package org.volunteer.handler.http.extension;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.volunteer.configuration.Config;
import org.volunteer.constant.Const;
import org.volunteer.handler.http.gen.ValueGen;
import org.volunteer.handler.http.param.ParamGetter;
import org.volunteer.util.JSONUtil;
import static org.volunteer.constant.Const.*;
import java.util.HashMap;
import java.util.Map;
public class ExtensionHandler0 extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Map<String, Object> param = ParamGetter.getRequestParams(msg);
		HttpRequest request = (HttpRequest) msg;
		System.out.println("plugin uri :"+request.uri());
		String interfaces = (String) param.get(Config.getString(INTERFACE));
		if (!request.uri().equals(interfaces)){ctx.fireChannelRead(request);return;}
		String username=(String)param.get("username");
		String password=(String)param.get("password");
		Map<String,Object> result = new HashMap<String,Object>();
		ValueGen valueGen = new ValueGen();
		result.put("message",valueGen.getValue(String.class));
		result.put("code",valueGen.getValue(Integer.class));
		String json = JSONUtil.mapToStr(result);
		byte[] bytes = json.getBytes();
		ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes, 0, bytes.length);
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK, byteBuf);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, Const.JSON);
		ctx.channel().writeAndFlush(response);
		;
	}
}
