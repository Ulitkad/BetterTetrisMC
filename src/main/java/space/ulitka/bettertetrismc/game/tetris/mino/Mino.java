package space.ulitka.bettertetrismc.game.tetris.mino;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Direction;
import space.ulitka.bettertetrismc.BetterTetrisMC;
import space.ulitka.bettertetrismc.config.TetrisConfig;
import space.ulitka.bettertetrismc.game.tetris.Controls;
import space.ulitka.bettertetrismc.game.tetris.HardDropAnimation;
import space.ulitka.bettertetrismc.screen.TetrisScreen;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public abstract class Mino {
    public Block[] b = new Block[4];
    public Block[] tempB = new Block[4];
    public String type = "";
    float dropCounter = 0;
    public int direction = 1;
    boolean leftCollision, rightCollision, bottomCollision;
    public boolean active = true;

    private final TetrisConfig config = TetrisConfig.loadConfig();

    public Mino() {
        switch (this) {
            case Mino_O ignored -> type = "o";
            case Mino_I ignored -> type = "i";
            case Mino_T ignored -> type = "t";
            case Mino_L ignored -> type = "l";
            case Mino_J ignored -> type = "j";
            case Mino_S ignored -> type = "s";
            case Mino_Z ignored -> type = "z";
            default -> {
            }
        }

        Pair<Identifier, MutableText> randomBlock = getRandomBlockTexture();
        create(randomBlock.getLeft(), randomBlock.getRight());
        //SpriteContents sprite = getRandomBlockTexture();
        //create(Identifier.of(sprite.getId().getNamespace().split(":")[0],"textures/" + sprite.getId().getPath() + ".png"), sprite.getWidth(), sprite.getHeight());
    }

    public void create(Identifier texture, MutableText name) {
        b[0] = new Block(texture, name, this.type);
        b[1] = new Block(texture, name, this.type);
        b[2] = new Block(texture, name, this.type);
        b[3] = new Block(texture, name, this.type);
        tempB[0] = new Block(texture, name, this.type);
        tempB[1] = new Block(texture, name, this.type);
        tempB[2] = new Block(texture, name, this.type);
        tempB[3] = new Block(texture, name, this.type);
    }

    public void setXY (int x, int y) {}
    public void updateXY (int direction) {
        for (Block b : tempB) {
            if (b.x < 0) {
                for (Block sB : TetrisScreen.staticBlocks) {
                    if (sB.x == b.x + Block.SIZE && sB.y == b.y) {
                        return;
                    }
                }
                for (Block block : tempB) {
                    block.x += Block.SIZE;
                }
            }
            if (b.x > TetrisScreen.WIDTH - Block.SIZE) {
                for (Block sB : TetrisScreen.staticBlocks) {
                    if (sB.x == b.x && sB.y == b.y) {
                        return;
                    }
                }
                for (Block block : tempB) {
                    block.x -= Block.SIZE;
                }
            }
            if (b.y > TetrisScreen.HEIGHT - Block.SIZE) return;
            for (Block sB : TetrisScreen.staticBlocks) {
                if (sB.x == b.x && sB.y == b.y) {
                    return;
                }
            }
        }
        this.direction = direction;
        b[0].x = tempB[0].x;
        b[0].y = tempB[0].y;
        b[1].x = tempB[1].x;
        b[1].y = tempB[1].y;
        b[2].x = tempB[2].x;
        b[2].y = tempB[2].y;
        b[3].x = tempB[3].x;
        b[3].y = tempB[3].y;
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvent.of(Identifier.ofVanilla("block.wooden_button.click_on")), 2.0F, config.tetris_volume));
    }
    public void getDirection1() {}
    public void getDirection2() {}
    public void getDirection3() {}
    public void getDirection4() {}
    public void checkMovementCollision() {
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        checkStaticBlockCollision();

        // check frame collision
        //left wall
        for (Block block : b) {
            if (block.x - Block.SIZE < 0) {
                leftCollision = true;
                break;
            }
        }
        //right wall
        for (Block block : b) {
            if (block.x + Block.SIZE >= TetrisScreen.WIDTH) {
                rightCollision = true;
                break;
            }
        }
        //bottom floor
        for (Block block : b) {
            if (block.y + Block.SIZE >= TetrisScreen.HEIGHT) {
                bottomCollision = true;
                break;
            }
        }
    }

    private void checkStaticBlockCollision() {
        for (Block staticBlock : TetrisScreen.staticBlocks) {

            //check down
            for (Block block : b) {
                if (block.y + Block.SIZE == staticBlock.y && block.x == staticBlock.x) {
                    bottomCollision = true;
                    break;
                }
            }
            //check left and right
            for (Block block : b) {
                if (block.x - Block.SIZE == staticBlock.x && block.y == staticBlock.y) {
                    leftCollision = true;
                    break;
                }
            }
            for (Block block : b) {
                if (block.x + Block.SIZE == staticBlock.x && block.y == staticBlock.y) {
                    rightCollision = true;
                    break;
                }
            }

        }
    }
    protected Pair<Identifier, MutableText> getRandomBlockTexture() {
        MinecraftClient client = MinecraftClient.getInstance();
        //net.minecraft.block.Block block;
        List<BakedQuad> quads;
        List<BlockModelPart> parts;
        SpriteContents texture;
        BlockState blockState;

        List<net.minecraft.block.Block> blocks = new java.util.ArrayList<>(Registries.BLOCK.stream().toList());
        Collections.shuffle(blocks);

        for (net.minecraft.block.Block block : blocks) {
            blockState = block.getStateManager().getStates().get(BetterTetrisMC.RANDOM.nextInt(block.getStateManager().getStates().size()));
            parts = client.getBlockRenderManager().getModel(blockState).getParts(BetterTetrisMC.RANDOM);
            if (parts.isEmpty()) continue;
            quads = parts.get(BetterTetrisMC.RANDOM.nextInt(parts.size())).getQuads(Direction.random(BetterTetrisMC.RANDOM));
            if (quads.isEmpty()) continue;

            texture = quads.get(BetterTetrisMC.RANDOM.nextInt(quads.size())).sprite().getContents();
            int corners = 0;
            if (!texture.isPixelTransparent(0, 0, 0)) corners++;
            if (!texture.isPixelTransparent(0, 15, 0)) corners++;
            if (!texture.isPixelTransparent(0, 0, 15)) corners++;
            if (!texture.isPixelTransparent(0, 15, 15)) corners++;
            if (corners < 3) continue;
            if (texture.getWidth()!=16) continue;
            return new Pair<>(texture.getId(), block.getName());
        }
        return new Pair<>(Identifier.ofVanilla("block/iron_block"), Blocks.IRON_BLOCK.getName());
    }
    public void update(float timePassed) {
        checkMovementCollision();
        if (Controls.leftMove) {
            checkMovementCollision();
            if (!leftCollision) {
                boolean proceed = true;
                for (Block block : b) {
                    for (Block sBlock : TetrisScreen.staticBlocks) {
                        if (block.x - Block.SIZE == sBlock.x && block.y == sBlock.y) {
                            proceed = false;
                            break;
                        }
                    }
                    if (block.x - Block.SIZE < 0) {
                        proceed = false;
                        break;
                    }
                }
                if (proceed) {
                    MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvent.of(Identifier.ofVanilla("block.wooden_button.click_on")), 2.0F,  config.tetris_volume));
                    for (Block block : b) {
                        block.x -= Block.SIZE;
                    }
                }
            }
            Controls.leftMove = false;
        }
        if (Controls.rightMove) {
            checkMovementCollision();
            if (!rightCollision) {
                boolean proceed = true;
                for (Block block : b) {
                    for (Block sBlock : TetrisScreen.staticBlocks) {
                        if (block.x + Block.SIZE == sBlock.x && block.y == sBlock.y) {
                            proceed = false;
                            break;
                        }
                    }
                    if (block.x + Block.SIZE > TetrisScreen.WIDTH) {
                        proceed = false;
                        break;
                    }
                }
                if (proceed) {
                    MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvent.of(Identifier.ofVanilla("block.wooden_button.click_on")), 2.0F,  config.tetris_volume));
                    for (Block block : b) {
                        block.x += Block.SIZE;
                    }
                }
            }
            Controls.rightMove = false;
        }
        if (Controls.clockwiseRotate) {
            switch (direction) {
                case 1: getDirection2(); break;
                case 2: getDirection3(); break;
                case 3: getDirection4(); break;
                case 4: getDirection1(); break;
            }
            Controls.clockwiseRotate = false;
        }
        if (Controls.counterclockwiseRotate) {
            switch (direction) {
                case 1: getDirection4(); break;
                case 2: getDirection1(); break;
                case 3: getDirection2(); break;
                case 4: getDirection3(); break;
            }
            Controls.counterclockwiseRotate = false;
        }
        if (Controls.uturnRotate) {
            switch (direction) {
                case 1: getDirection3(); break;
                case 2: getDirection4(); break;
                case 3: getDirection1(); break;
                case 4: getDirection2(); break;
            }
            Controls.uturnRotate = false;
        }
        if (Controls.softDropMove) {
            if (bottomCollision) {
                dropCounter += 30;
            } else {
                boolean proceed = true;
                for (Block block : b) {
                    for (Block b : TetrisScreen.staticBlocks) {
                        if (block.y + Block.SIZE == b.y && block.x == b.x) {
                            proceed = false;
                            break;
                        }
                    }
                }
                if (proceed) {
                    MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvent.of(Identifier.ofVanilla("block.wooden_button.click_on")), 2.0F, config.tetris_volume));
                    for (Block block : b) {
                        block.y += Block.SIZE;
                    }
                    TetrisScreen.score++;
                    dropCounter = 0;
                }
            }
            Controls.softDropMove = false;
        }
        if (Controls.hardDropMove && TetrisScreen.hardDrop > 0) {
            MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvent.of(Identifier.ofVanilla("entity.wind_charge.wind_burst")), 1.0F,  config.tetris_volume / 2));
            int drop = getDropOffset();
            TetrisScreen.animations.add(new HardDropAnimation(b[0].x, b[0].y, 27, drop, 10));
            b[0].y += drop;
            b[1].y += drop;
            b[2].y += drop;
            b[3].y += drop;
            Controls.hardDropMove = false;
            this.active = false;
            TetrisScreen.score += 2 * drop / Block.SIZE;
            return;
        }
        dropCounter = dropCounter + timePassed;
        if (Math.floor(dropCounter) >= TetrisScreen.dropInterval) {
            if (bottomCollision) {
                checkStaticBlockCollision();
                checkMovementCollision();
                if (bottomCollision) active = false;
            } else {
                boolean proceed = true;
                for (Block block : b) {
                    for (Block b : TetrisScreen.staticBlocks) {
                        if (block.y + Block.SIZE == b.y && block.x == b.x) {
                            proceed = false;
                            break;
                        }
                    }
                }
                if (proceed) {
                    for (Block block : b) {
                        block.y += Block.SIZE;
                    }
                    dropCounter = 0;
                }
            }
        }
    }
    public void draw (DrawContext context) {
        for (Block block : b) {
            block.draw(context);
        }
    }
    public void drawHardDrop (DrawContext context) {
        int yOffset = getDropOffset();

        Color color = new Color(1, 1, 1, 0.5f);
        for (Block block : b) {
            switch (TetrisScreen.hardDrop) {
                case 2:
                if (getOutline(block)[0]) {
                    context.drawHorizontalLine(TetrisScreen.left_x + block.x, TetrisScreen.left_x + block.x + Block.SIZE - 1, TetrisScreen.top_y + block.y + yOffset, color.getRGB());
                }
                if (getOutline(block)[1]) {
                    context.drawHorizontalLine(TetrisScreen.left_x + block.x, TetrisScreen.left_x + block.x + Block.SIZE - 1, TetrisScreen.top_y + block.y + Block.SIZE - 1 + yOffset, color.getRGB());
                }
                if (getOutline(block)[2]) {
                    context.drawVerticalLine(TetrisScreen.left_x + block.x, TetrisScreen.top_y + block.y + yOffset, TetrisScreen.top_y + block.y + Block.SIZE - 1 + yOffset, color.getRGB());
                    if (!getOutline(block)[1]) {
                        context.drawVerticalLine(TetrisScreen.left_x + block.x, TetrisScreen.top_y + block.y + Block.SIZE + yOffset - 1, TetrisScreen.top_y + block.y + Block.SIZE + yOffset - 1, color.getRGB());
                    }
                    if (!getOutline(block)[0]) {
                        context.drawVerticalLine(TetrisScreen.left_x + block.x, TetrisScreen.top_y + block.y + yOffset, TetrisScreen.top_y + block.y + yOffset, color.getRGB());
                    }
                }
                if (getOutline(block)[3]) {
                    context.drawVerticalLine(TetrisScreen.left_x + block.x + Block.SIZE - 1, TetrisScreen.top_y + block.y + yOffset, TetrisScreen.top_y + block.y + Block.SIZE - 1 + yOffset, color.getRGB());
                    if (!getOutline(block)[1]) {
                        context.drawVerticalLine(TetrisScreen.left_x + block.x + Block.SIZE - 1, TetrisScreen.top_y + block.y + Block.SIZE + yOffset - 1, TetrisScreen.top_y + block.y + Block.SIZE + yOffset - 1, color.getRGB());
                    }
                    if (!getOutline(block)[0]) {
                        context.drawVerticalLine(TetrisScreen.left_x + block.x + Block.SIZE - 1, TetrisScreen.top_y + block.y + yOffset, TetrisScreen.top_y + block.y + yOffset, color.getRGB());
                    }
                }
                //individual diagonal pixels
                if (!(this instanceof Mino_O)) {
                    if (!getOutline(block)[0] && !getOutline(block)[2]) {
                        context.drawVerticalLine(TetrisScreen.left_x + block.x, TetrisScreen.top_y + block.y + yOffset, TetrisScreen.top_y + block.y + yOffset, color.getRGB());
                    }
                    if (!getOutline(block)[0] && !getOutline(block)[3]) {
                        context.drawVerticalLine(TetrisScreen.left_x + block.x + Block.SIZE - 1, TetrisScreen.top_y + block.y + yOffset, TetrisScreen.top_y + block.y + yOffset, color.getRGB());
                    }
                    if (!getOutline(block)[1] && !getOutline(block)[2]) {
                        context.drawVerticalLine(TetrisScreen.left_x + block.x, TetrisScreen.top_y + block.y + Block.SIZE + yOffset - 1, TetrisScreen.top_y + block.y + Block.SIZE + yOffset - 1, color.getRGB());
                    }
                    if (!getOutline(block)[1] && !getOutline(block)[3]) {
                        context.drawVerticalLine(TetrisScreen.left_x + block.x + Block.SIZE - 1, TetrisScreen.top_y + block.y + Block.SIZE + yOffset - 1, TetrisScreen.top_y + block.y + Block.SIZE + yOffset - 1, color.getRGB());
                    }
                } break;
                case 3: block.draw(context, yOffset); break;
            }
        }
    }

    private int getDropOffset() {
        int i;
        for (i = 0; i < TetrisScreen.HEIGHT / Block.SIZE; i++) {
            for (Block b : b) {
                for (Block sB : TetrisScreen.staticBlocks) {
                    if (b.x == sB.x && b.y + i * Block.SIZE == sB.y) {
                        return i * Block.SIZE - Block.SIZE;
                    }
                }
                if (b.y + i * Block.SIZE > TetrisScreen.HEIGHT - Block.SIZE) {
                    return i * Block.SIZE - Block.SIZE;
                }
            }
        }
        return i * Block.SIZE - Block.SIZE;
    }

    private boolean[] getOutline(Block b) {
        boolean top = true;
        boolean bottom = true;
        boolean left = true;
        boolean right = true;
        for (Block block : this.b) {
            if (b.y - Block.SIZE == block.y && b.x == block.x) {
                top = false;
            }
            if (b.y + Block.SIZE == block.y && b.x == block.x) {
                bottom = false;
            }
            if (b.x - Block.SIZE == block.x && b.y == block.y) {
                left = false;
            }
            if (b.x + Block.SIZE == block.x && b.y == block.y) {
                right = false;
            }
        }
        return new boolean[]{top, bottom, left, right};
    }
}
