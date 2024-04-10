package com.triade.simple.core.bridges.domain;

public enum BridgePositionEnum {

    BASE {
        @Override
        public Bridge doPositionTransition(Bridge bridge) {
            return bridge.toBuilder()
                .position(BridgePositionEnum.HIGHEST)
                .direction(BridgeDirectionEnum.DOWN)
                .status(BridgeStatusEnum.STOPPED)
                .build();
        }

        @Override
        public BridgeDirectionEnum getDirection() {
            return BridgeDirectionEnum.DOWN;
        }
    },
    MIDDLE {
        @Override
        public Bridge doPositionTransition(Bridge bridge) {
            if (bridge.getStatus() == BridgeStatusEnum.MOVING) {
                var direction = bridge.getDirection() == BridgeDirectionEnum.UP
                    ? BridgeDirectionEnum.DOWN
                    : BridgeDirectionEnum.UP;

                return bridge.toBuilder()
                    .position(BridgePositionEnum.MIDDLE)
                    .direction(direction)
                    .status(BridgeStatusEnum.STOPPED)
                    .build();
            }

            if (bridge.getDirection() == BridgeDirectionEnum.DOWN) {
                return bridge.toBuilder()
                    .position(BridgePositionEnum.BASE)
                    .direction(BridgeDirectionEnum.UP)
                    .status(BridgeStatusEnum.STOPPED)
                    .build();
            }

            return bridge.toBuilder()
                .position(BridgePositionEnum.HIGHEST)
                .direction(BridgeDirectionEnum.DOWN)
                .status(BridgeStatusEnum.STOPPED)
                .build();
        }

        @Override
        public BridgeDirectionEnum getDirection() {
            return null;
        }
    },
    HIGHEST {
        @Override
        public Bridge doPositionTransition(Bridge bridge) {
            return bridge.toBuilder()
                .position(BridgePositionEnum.BASE)
                .direction(BridgeDirectionEnum.UP)
                .status(BridgeStatusEnum.STOPPED)
                .build();
        }

        @Override
        public BridgeDirectionEnum getDirection() {
            return BridgeDirectionEnum.UP;
        }
    }
    ;

    public abstract Bridge doPositionTransition(Bridge bridge);

    public abstract BridgeDirectionEnum getDirection();
}
