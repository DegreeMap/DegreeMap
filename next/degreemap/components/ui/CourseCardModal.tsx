import React, { useState } from "react"
import { Button } from "./button";

interface CourseCardModalProps {
	selectedCourses: Course[]
    isOpen: boolean
    onClose: () => void
    onSave: (updated: { title: string; code: string; credits: number }) => void;
}

export const CourseCardModal: React.FC<CourseCardModalProps>= ({ 
        // course, 
        isOpen, 
        onClose, 
        onSave 
    }) => {
    const [title, setTitle] = useState("");
	const [code, setCode] = useState("");
	const [credits, setCredits] = useState((0).toString());

	if (!isOpen){
        return null;
    } else {
        return (
            <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50"
                 onClick={onClose}>
                <div className="bg-white p-4 rounded shadow-md w-80"
                     onClick={(e) => e.stopPropagation()}>
                    <h2 className="text-lg text-gray-600 font-semibold mb-2">Edit Course</h2>
                    <input
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        className="text-gray-600 w-full mb-2 border px-2 py-1 rounded"
                        placeholder="Title"
                    />
                    <input
                        value={code}
                        onChange={(e) => setCode(e.target.value)}
                        className="text-gray-600 w-full mb-2 border px-2 py-1 rounded"
                        placeholder="Code"
                    />
                    <input
                        type="number"
                        value={credits}
                        onChange={(e) => setCredits(e.target.value)}
                        className="text-gray-600 w-full mb-4 border px-2 py-1 rounded"
                        placeholder="Credits"
                    />
                    <div className="flex justify-end gap-2">
                        <Button onClick={onClose} className="bg-gray-600">
                            Cancel
                        </Button>
                        <Button
                            onClick={() => {
                                onSave({ title, code, credits: Number(credits) })
                                onClose()
                            }}
                        >
                            Save
                        </Button>
                    </div>
                </div>
            </div>
        );
    }
}