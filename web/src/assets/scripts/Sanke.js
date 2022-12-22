import { BaseGameObject } from "./BaseGameObject";
import { Cell } from "./Cell";

export class Snake extends BaseGameObject {
    constructor(info, gamemap){
        super();

        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;

        // snake body, cells[0] = snake head
        this.cells = [new Cell(info.r, info.c)];

        // move speed cells per second
        this.speed = 5;

        // -1 = no instruction, 0 = up, 1 = right, 2 = down, 3 = left
        this.direction = -1;
        // idle = still, move = moving, die = dead;
        this.status = "idle";

        // next destination
        this.next_cell = null;

        this.dr = [-1, 0, 1, 0];
        this.dc = [0, 1, 0, -1];

        this.step = 0;

        this.eps = 1e-2;
    }

    start(){

    }

    set_direction(d){
        this.direction = d;
    }

    check_tail_increasing() {
        if(this.step <= 10) return true;

        if(this.step % 3 === 1) return true;

        return false;
    }

    next_step(){
        const d = this.direction;

        this.next_cell = new Cell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d]);
        this.direction = -1;
        this.status = "move";

        this.step ++;

        const k = this.cells.length;
        for(let i = k; i > 0; i--){
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i-1]));
        }
    }

    update_move() {
        // this.cells[0].x += this.speed * this.timedelta / 1000;
        
        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;
        const distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < this.eps) {
            // arrive at destination
            this.cells[0] = this.next_cell;
            this.next_cell = null;

            this.status = "idle";

            if(!this.check_tail_increasing()){
                this.cells.pop();
            }
        }else{
            const move_distance = this.speed * this.timedelta / 1000;
            this.cells[0].x += move_distance * dx / distance;
            this.cells[0].y += move_distance * dy / distance;

            if(!this.check_tail_increasing()){
                // move tail
                const k = this.cells.length;
                const tail = this.cells[k-1];
                const tail_target = this.cells[k-2];
                const tail_dx = tail_target.x - tail.x;
                const tail_dy = tail_target.y - tail.y;
                tail.x += move_distance * tail_dx / distance;
                tail.y += move_distance * tail_dy / distance;
            }
        }
    }

    update(){
        if(this.status === "move"){
            this.update_move();
        } 

        this.render();
    }

    render(){
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        ctx.fillStyle = this.color;
        for(const cell of this.cells){
            ctx.beginPath();
            ctx.arc(cell.x*L, cell.y*L, L/2, 0, Math.PI*2);
            ctx.fill();
        }
    }
}