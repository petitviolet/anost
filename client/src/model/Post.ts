export class Post {
  readonly id: string;
  readonly ownerId: string;
  readonly title: string;
  readonly fileType: string;
  readonly contents: string;
  readonly comments: Comment[];
}

export class Comment {
  readonly id: string;
  readonly owner: CommentOwner;
  readonly postId: string;
  readonly sentence: string;
}

export class CommentOwner {
  readonly userId: string;
  readonly userName: string;
}