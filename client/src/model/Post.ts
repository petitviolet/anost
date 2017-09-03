export class Post {
  readonly id: string;
  readonly ownerId: string;
  readonly title: string;
  readonly fileType: string;
  readonly contents: string;
  readonly comments: [Comment];
}

export class Comment {
  readonly userId: string;
  readonly postId: string;
  readonly sentence: string;
}