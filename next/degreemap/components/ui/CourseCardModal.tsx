import React, { useState } from "react"

interface CourseCardModalProps {
	course: {
        title: string
        code: string
        credits: number
    }
    isOpen: boolean
    onClose: () => void
    onSave: (updated: { title: string; code: string; credits: number }) => void;
}

export const CourseCardModal: React.FC<CourseCardModalProps>= ({ course, isOpen, onClose, onSave }) => {
    const [title, setTitle] = useState(course.title);
	const [code, setCode] = useState(course.code);
	const [credits, setCredits] = useState((course.credits??0).toString());

	if (!isOpen){
        return null;
    } else {
        return (
            <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
                <div className="bg-white p-4 rounded shadow-md w-80">
                    <h2 className="text-lg font-semibold mb-2">Edit Course</h2>
                    <input
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        className="w-full mb-2 border px-2 py-1 rounded"
                        placeholder="Title"
                    />
                    <input
                        value={code}
                        onChange={(e) => setCode(e.target.value)}
                        className="w-full mb-2 border px-2 py-1 rounded"
                        placeholder="Code"
                    />
                    <input
                        type="number"
                        value={credits}
                        onChange={(e) => setCredits(e.target.value)}
                        className="w-full mb-4 border px-2 py-1 rounded"
                        placeholder="Credits"
                    />
                    <div className="flex justify-end gap-2">
                        <button onClick={onClose} className="px-3 py-1 border rounded">
                            Cancel
                        </button>
                        <button
                            onClick={() => onSave({ title, code, credits: Number(credits) })}
                            className="px-3 py-1 bg-blue-600 text-white rounded"
                        >
                            Save
                        </button>
                    </div>
                </div>
            </div>
        );
    }
}