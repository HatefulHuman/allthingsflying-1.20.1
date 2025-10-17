package com.kfhzs.allthingsflying.animation.player;

public class PlayerAnimationTrigger {
//    public static void onPlayerRender(RenderPlayerEvent.Pre event) {
//        AbstractClientPlayer player = (AbstractClientPlayer) event.getEntity();
//
//        var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(player).get(Objects.requireNonNull(ResourceLocation.tryBuild(FeiXingFaQi.MODID, "animation")));
//
//        if (animation != null) {
//            boolean isRidingSword = player.getVehicle() instanceof FlyingMagicWeapon;
//
//            if (isRidingSword) {
//                FlyingMagicWeapon weapon = (FlyingMagicWeapon) player.getVehicle();
//                String actionName = weapon.getDataIdPlayerAction();
//
//                var keyframeAnimation = PlayerAnimationRegistry.getAnimation(Objects.requireNonNull(ResourceLocation.tryBuild(FeiXingFaQi.MODID, actionName)));
//                if (keyframeAnimation != null) {
//                    if (animation.getAnimation() instanceof KeyframeAnimationPlayer currentPlayer) {
//                        if (!currentPlayer.getData().equals(keyframeAnimation)) {
//                            animation.replaceAnimationWithFade(
//                                    AbstractFadeModifier.standardFadeIn(5, Ease.INOUTSINE),
//                                    new KeyframeAnimationPlayer(keyframeAnimation)
//                            );
//                        }
//                    } else {
//                        animation.setAnimation(
//                                new KeyframeAnimationPlayer(keyframeAnimation)
//                        );
//                    }
//                }
//            } else {
//                if (animation.isActive()) {
//                    animation.replaceAnimationWithFade(
//                            AbstractFadeModifier.standardFadeIn(5, Ease.INOUTSINE),
//                            null
//                    );
//                }
//            }
//        }
//    }
//    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
//        if (Minecraft.getInstance().player.getUUID().equals(event.player.getUUID())) {
//            AbstractClientPlayer player = Minecraft.getInstance().player;
//            var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(player).get(Objects.requireNonNull(ResourceLocation.tryBuild(FeiXingFaQi.MODID, "animation")));
//
//            if (animation != null) {
//                boolean isRidingSword = player.getVehicle() instanceof FlyingMagicWeapon;
//
//                if (isRidingSword) {
//                    FlyingMagicWeapon weapon = (FlyingMagicWeapon) player.getVehicle();
//                    String actionName = weapon.getDataIdPlayerAction();
//
//                    var keyframeAnimation = PlayerAnimationRegistry.getAnimation(Objects.requireNonNull(ResourceLocation.tryBuild(FeiXingFaQi.MODID, actionName)));
//                    if (keyframeAnimation != null) {
//                        if (animation.getAnimation() instanceof KeyframeAnimationPlayer currentPlayer) {
//                            currentPlayer.setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL);
//                            currentPlayer.setFirstPersonConfiguration(new FirstPersonConfiguration(true, true, true, true));
//                            if (!currentPlayer.getData().equals(keyframeAnimation)) {
//                                animation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(5, Ease.INOUTSINE), currentPlayer);
//                            }
//                        } else {
//                            animation.setAnimation(new KeyframeAnimationPlayer(keyframeAnimation));
//                        }
//                    }
//                } else {
//                    if (animation.isActive()) {
//                        animation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(5, Ease.INOUTSINE), null);
//                    }
//                }
//            }
//        }
//    }
}
