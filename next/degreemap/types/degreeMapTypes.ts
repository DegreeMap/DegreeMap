interface Course {
	id: number;
	title: string;
	code: string;
	credits: number;
	color: string;
}

interface Block {
	id: number;
	title: string;
	color: string;
}

interface Term {
	id: number;
	name: string;
	courses: Course[];
	blocks: Block[];
}

interface Year {
	id: number;
	name: string;
	terms: Term[];
}