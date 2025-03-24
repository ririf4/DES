package net.ririfa.des.manager

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler
import io.papermc.paper.event.player.PlayerChangeBeaconEffectEvent
import net.ririfa.des.Logger
import org.bukkit.Effect
import org.bukkit.EntityEffect
import org.bukkit.inventory.Inventory
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionEffectTypeCategory

class WS(
	private val port: Int,
	private var channel: Channel? = null,
	private var bossGroup: EventLoopGroup? = null,
	private var workerGroup: EventLoopGroup? = null
) {
	fun start() {
		bossGroup = NioEventLoopGroup(1)
		workerGroup = NioEventLoopGroup()

		val bootStrap = ServerBootstrap()
			.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel::class.java)
			.childHandler(object : ChannelInitializer<SocketChannel>() {
				override fun initChannel(ch: SocketChannel) {
					ch.pipeline().apply {
						addLast(HttpServerCodec())

						addLast(HttpObjectAggregator(524288))

						addLast(WebSocketServerProtocolHandler("/ws"))

						addLast(object : SimpleChannelInboundHandler<BinaryWebSocketFrame>() {
							override fun messageReceived(
								ctx: ChannelHandlerContext,
								frame: BinaryWebSocketFrame
							) {
								val byteBuf = frame.content()
								val bytes = ByteArray(byteBuf.readableBytes())
								byteBuf.readBytes(bytes)

								val jsonString = String(bytes)
								//TODO
							}

						})
					}
				}
			})

		channel = bootStrap.bind(port).sync().channel()
		Logger.info(TODO())
	}

	fun stop() {
		channel?.close()?.sync()
		bossGroup?.shutdownGracefully()
		workerGroup?.shutdownGracefully()
	}

	data class PlayerData(
		val health: Int,
		val effects: List<PotionEffectType>,
		val inventory: Inventory,
		val food: Int,
		val hiddenFood: Int,
		val experience: Float,
		// 酸素ゲージ。型は分からん
		val o2: Double
	)
}
